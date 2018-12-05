package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="Climber Test", group="14465")

public class ClimberTest extends OpMode {

    private DcMotor lFDrive = null;
    private DcMotor lRDrive = null;
    private DcMotor rFDrive = null;
    private DcMotor rRDrive = null;

    private DcMotor climber = null;
    private DcMotor arm = null;

    private Servo arm1 = null;
    private Servo arm2 = null;

    private ElapsedTime runTime = new ElapsedTime();

    @Override
    public void init() {
        lFDrive = hardwareMap.get(DcMotor.class, "LFDrive");
        lRDrive = hardwareMap.get(DcMotor.class, "LRDrive");
        rFDrive = hardwareMap.get(DcMotor.class, "RFDrive");
        rRDrive = hardwareMap.get(DcMotor.class, "RRDrive");

        climber = hardwareMap.get(DcMotor.class, "Climber");
        arm = hardwareMap.get(DcMotor.class, "Arm");

        arm1 = hardwareMap.get(Servo.class, "Arm1");
        arm2 = hardwareMap.get(Servo.class, "Arm2");

        arm2.setDirection(Servo.Direction.REVERSE);

        rFDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rRDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");

        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void start(){
        runTime.reset();
        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

    }

    @Override
    public void loop() {
        int length = climber.getCurrentPosition();
        telemetry.addData("Climber Encoder", length);
        telemetry.addData("Time Elapsed", runTime.seconds());
        mecanumDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);
        climber(0, 0);
        armControl();
        telemetry.addData("Arm1", arm1.getPosition());
        telemetry.addData("Arm2", arm2.getPosition());

        telemetry.update();
    }

    public void armControl(){
        if(gamepad1.a){
            arm1.setPosition(.9);
            arm2.setPosition(.9);
        }
        else if(gamepad1.b){
            arm1.setPosition(.03);
            arm2.setPosition(.03);
        }

    }

    public void climber(double speed, int length){
        //if(runTime.seconds() > 75){

        //}

        if(gamepad1.dpad_up){
            climber.setPower(1);
        }
        else if(gamepad1.dpad_down){
            climber.setPower(-1);
        }
        else{
            climber.setPower(0);
        }


    }

    public void mecanumDrive(double x, double y, double rotate){
        double wheelSpeeds[] = new double[4];

        wheelSpeeds[0] = x + y + rotate;
        wheelSpeeds[1] = -x + y - rotate;
        wheelSpeeds[2] = -x + y + rotate;
        wheelSpeeds[3] = x + y - rotate;

        normalize(wheelSpeeds);

        lFDrive.setPower(wheelSpeeds[0]);
        rFDrive.setPower(wheelSpeeds[1]);
        lRDrive.setPower(wheelSpeeds[2]);
        rRDrive.setPower(wheelSpeeds[3]);
    }

    private void normalize(double[] wheelSpeeds){
        double maxMagnitude = Math.abs(wheelSpeeds[0]);

        for (int i = 1; i < wheelSpeeds.length; i++){
            double magnitude = Math.abs(wheelSpeeds[i]);

            if (magnitude > maxMagnitude){
                maxMagnitude = magnitude;
            }
        }

        if (maxMagnitude > 1.0){
            for (int i = 0; i < wheelSpeeds.length; i++){
                wheelSpeeds[i] /= maxMagnitude;
            }
        }

    }
}
