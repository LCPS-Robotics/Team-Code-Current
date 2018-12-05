package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@TeleOp
public class FinalRobotCode extends OpMode {

    private ElapsedTime runTime = new ElapsedTime();

    private boolean driveTank = true;
    double toggleTime;

    double climbPos = .17;

    //leftDrive Motors
    private DcMotor lFDrive = null;
    private DcMotor lRDrive = null;

    //rightDrive Motors
    private DcMotor rFDrive = null;
    private DcMotor rRDrive = null;

    //Arm Motors
    private DcMotor upArm = null;
    private DcMotor lowArm = null;

    //Bucket Motors
    private DcMotor bucketAngle = null;
    private DcMotor bucketFlaps = null;

    //Climbing Servos
    private Servo lift1 = null;
    private Servo lift2 = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        lFDrive = hardwareMap.get(DcMotor.class, "LFDrive");
        lRDrive = hardwareMap.get(DcMotor.class, "LRDrive");
        rFDrive = hardwareMap.get(DcMotor.class, "RFDrive");
        rRDrive = hardwareMap.get(DcMotor.class, "RRDrive");

        upArm = hardwareMap.get(DcMotor.class, "UpArm");
        lowArm = hardwareMap.get(DcMotor.class, "LowArm");

        bucketAngle = hardwareMap.get(DcMotor.class, "bucketAngle");
        bucketFlaps = hardwareMap.get(DcMotor.class, "bucketFlap");

        lift1 = hardwareMap.get(Servo.class, "lift1");
        lift2 = hardwareMap.get(Servo.class, "lift2");

        rFDrive.setDirection(DcMotor.Direction.REVERSE);
        rRDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void start() {
        runTime.reset();
        toggleTime = runTime.seconds();
    }

    @Override
    public void loop() {
        if (driveTank) {
            tankDrive();
        } else {
            arcadeDrive();
        }

        if (gamepad1.start) {
            toggle();
        }

        climb();
        bucket();
        arm();
    }

    public void arm(){
        //low Arm code
        if(gamepad1.left_trigger > .5){
            lowArm.setPower(.5);
        }
        else if(gamepad1.left_bumper){
            lowArm.setPower(-.5);
        }
        else{
            lowArm.setPower(0);
        }

        //up Arm code
        if(gamepad1.right_trigger > .5){
            upArm.setPower(.5);
        }
        else if(gamepad1.right_bumper){
            upArm.setPower(-.5);
        }
        else{
            upArm.setPower(0);
        }
    }


    public void climb() {
        if (gamepad1.dpad_up && climbPos < .82) {
            climbPos += .05;
        } else if (gamepad1.dpad_down && climbPos > .17) {
            climbPos -= .05;
        }
        lift1.setPosition(climbPos);
        lift2.setPosition(climbPos);
    }

    public void bucket() {
        if (gamepad1.a) {
            bucketFlaps.setPower(1);
        } else if (gamepad1.b) {
            bucketFlaps.setPower(-1);
        }

        if(gamepad1.x){
            bucketAngle.setPower(.25);
        }
        else if(gamepad1.y){
            bucketAngle.setPower(-.25);
        }
        else{
            bucketAngle.setPower(0);
        }
    }

    public void toggle() {
        if (toggleTime < runTime.seconds() - 1) {
            if (driveTank) {
                driveTank = false;
            } else {
                driveTank = true;
            }
        }

        toggleTime = runTime.seconds();
    }

    public void tankDrive() {
        double leftPower;
        double rightPower;

        leftPower = gamepad1.left_stick_y;
        rightPower = gamepad1.right_stick_y;

        //deadZone
        if (leftPower < .1 && leftPower > -.1) {
            leftPower = 0;
        }

        if (rightPower < .1 && rightPower > -.1) {
            rightPower = 0;
        }

        lFDrive.setPower(leftPower);
        lRDrive.setPower(leftPower);

        rFDrive.setPower(rightPower);
        rRDrive.setPower(rightPower);
    }

    public void arcadeDrive() {
        double powY;
        double powX;
        double leftPower;
        double rightPower;

        powY = gamepad1.left_stick_y;
        powX = gamepad1.left_stick_x;

        if (powX < -.05) {
            leftPower = powX;
            lRDrive.setPower(leftPower);
            lFDrive.setPower(leftPower);

            rightPower = -powX;
            rFDrive.setPower(rightPower);
            rRDrive.setPower(rightPower);
        } else if (powX > .05) {
            leftPower = powX;
            lRDrive.setPower(leftPower);
            lFDrive.setPower(leftPower);

            rightPower = -powX;
            rFDrive.setPower(rightPower);
            rRDrive.setPower(rightPower);
        } else {
            leftPower = powY;
            rightPower = powY;

            lFDrive.setPower(leftPower);
            lRDrive.setPower(leftPower);
            rFDrive.setPower(rightPower);
            rRDrive.setPower(rightPower);
        }
    }
}