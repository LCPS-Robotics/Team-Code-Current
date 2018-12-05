package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Mecanum Robot", group="Teleop")
public class RobotMecanum extends OpMode {

    private DcMotor lFDrive = null;
    private DcMotor lRDrive = null;
    private DcMotor rFDrive = null;
    private DcMotor rRDrive = null;

    private DcMotor climber = null;
    private DcMotor arm = null;

    private ElapsedTime runTime = new ElapsedTime();

    @Override
    public void init() {
        lFDrive = hardwareMap.get(DcMotor.class, "LFDrive");
        lRDrive = hardwareMap.get(DcMotor.class, "LRDrive");
        rFDrive = hardwareMap.get(DcMotor.class, "RFDrive");
        rRDrive = hardwareMap.get(DcMotor.class, "RRDrive");

        climber = hardwareMap.get(DcMotor.class, "Climber");
        arm = hardwareMap.get(DcMotor.class, "Arm");

        rFDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rRDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start(){
        runTime.reset();
        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void loop() {
        mecanumDrive(gamepad1.left_stick_x,-gamepad1.left_stick_y, -gamepad1.right_stick_x);
        climber(0, 0);
    }

    public void climber(double speed, int length){
        telemetry.addData("Climber Encoder", climber.getCurrentPosition());
        if(runTime.seconds() > 75){

        }

        if(gamepad2.dpad_up){
            climber.setPower(.5);
        }
        else if(gamepad2.dpad_down){
            climber.setPower(-.5);
        }
        else{
            climber.setPower(0);
        }

        telemetry.update();
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
