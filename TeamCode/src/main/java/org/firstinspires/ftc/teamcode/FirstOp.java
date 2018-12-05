package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Thread.sleep;

@TeleOp
public class FirstOp extends OpMode{

    private ElapsedTime runTime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor sweeper = null;
    private Servo scoop = null;
    private DcMotor arm = null;

    static final double INCREMENT = 0.01;
    static final int CYCLE_MS = 50;
    private double position = (1.0 - 0.0)/2;


    //add sweeper 1 motor, 1 servo for claw, 1 motor for arm

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        sweeper = hardwareMap.get(DcMotor.class, "sweeper");
        scoop = hardwareMap.get(Servo.class, "scoop");
        arm = hardwareMap.get(DcMotor.class, "arm");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        sweeper.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void start(){
        runTime.reset();
        scoop.setPosition(position);
    }

    @Override
    public void loop() {
        double leftPower;
        double rightPower;
        double sweeperPower;
        double armPower;

        if(gamepad2.right_trigger > 0.0){
            armPower = gamepad2.right_trigger;
        }
        else if (gamepad2.left_trigger > 0.0){
            armPower = -gamepad2.left_trigger;
        }
        else{
            armPower = 0.0;
        }

        arm.setPower(armPower);

        leftPower = gamepad1.left_stick_y;
        rightPower = gamepad1.right_stick_y;

        if (gamepad2.right_bumper){
            // this is 1/4 power sweeper speed
            sweeperPower = 1;
        }
        else if (gamepad2.left_bumper){
            // this is 1/4 power sweeper speed
            sweeperPower = -1;
        }
        else{
            sweeperPower = 0;
        }

        //scoop control

        if (gamepad2.dpad_right){
            position -= INCREMENT;
            if(position < 0.0){
                position = 0.0;
            }
        }
        else if (gamepad2.dpad_left){
            position += INCREMENT;
            if(position > 1.0){
                position = 1.0;
            }
        }
        else if (gamepad2.dpad_up){
            position -= INCREMENT;
            if(position < 0.0){
                position = 0.0;
            }
        }
        else if (gamepad2.dpad_down){
            position += INCREMENT;
            if(position > 1.0){
                position = 1.0;
            }

        }
        else{
            position = position;
        }

        //deadzone
        if(leftPower < .1 && leftPower > -.1){
            leftPower = 0;
        }

        if (rightPower < .1 && rightPower > -.1){
            rightPower = 0;
        }

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        sweeper.setPower(sweeperPower);
        scoop.setPosition(position);



        telemetry.addData("Status", "Run Time: " + runTime.toString());

    }
}