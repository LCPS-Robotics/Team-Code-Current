package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.sql.Driver;

@Autonomous(name="Gold Auto", group="14465")
public class goldAuton extends LinearOpMode {
    // todo: write your code here
    //lFDrive Motors
    private DcMotor lFDrive = null;
    private DcMotor lRDrive = null;

    //rFDrive Motors
    private DcMotor rFDrive = null;
    private DcMotor rRDrive = null;

    //Arm Motors
    private DcMotor lowArm = null;

    private ElapsedTime runTime = new ElapsedTime();

    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_MOTOR_REV = 1440;
    static final double DRIVE_GEAR_REDUCTION = 1.0;

    static final double DRIVE_SPEED = 0.4;
    static final double TURN_SPEED = 0.3;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() throws InterruptedException {
        lFDrive = hardwareMap.get(DcMotor.class, "LFDrive");
        lRDrive = hardwareMap.get(DcMotor.class, "LRDrive");

        rFDrive = hardwareMap.get(DcMotor.class, "RFDrive");
        rRDrive = hardwareMap.get(DcMotor.class, "RRDrive");

        //lowArm = hardwareMap.get(DcMotor.class, "LowArm");

        rFDrive.setDirection(DcMotor.Direction.REVERSE);
        rRDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        lFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path0", "Starting at %7d : %7d", lFDrive.getCurrentPosition(), rFDrive.getCurrentPosition());
        telemetry.update();

        waitForStart();

        //climbDown();
        //encoderDrive(DRIVE_SPEED, 6, 6, 3.0);
        //encoderDrive(TURN_SPEED, -12, 12, 3.0);

        mecanumDrive(1.0, 36,  5.0);
        //encoderDrive(DRIVE_SPEED, -45, -45, 12.0);
        //encoderDrive(DRIVE_SPEED, 9, -9, 6.0);
        //encoderDrive(DRIVE_SPEED, -45, -45, 10.0);
        //encoderDrive(1.0, -24, 24, 20.0);

    }

    public void armControl(double speed, int position) {
        lowArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //if low arm
        lowArm.setTargetPosition(position);
        lowArm.setPower(speed);

        while (lowArm.isBusy()) {

        }
        lowArm.setPower(0);

        lowArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void climbDown() {

    }

    public void mecanumDrive(double strafeSpeed, double strafeDistance, double timeoutS){
        int newStrafeTarget;

        lFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (opModeIsActive()){
            newStrafeTarget = lFDrive.getCurrentPosition() + (int)(strafeDistance * COUNTS_PER_INCH);
            lFDrive.setTargetPosition(newStrafeTarget);
            lRDrive.setTargetPosition(-newStrafeTarget);
            rFDrive.setTargetPosition(-newStrafeTarget);
            rRDrive.setTargetPosition(newStrafeTarget);

            lFDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rFDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runTime.reset();
            lFDrive.setPower(Math.abs(strafeSpeed));
            rFDrive.setPower(Math.abs(strafeSpeed));
            lRDrive.setPower(Math.abs(strafeSpeed));
            rRDrive.setPower(Math.abs(strafeSpeed));

            while (opModeIsActive() &&
                    (runTime.seconds() < timeoutS) &&
                    (lFDrive.isBusy() && rFDrive.isBusy())) {
                telemetry.addData("Path2", "Running at %7d :%7d", lFDrive.getCurrentPosition(), rFDrive.getCurrentPosition());
                telemetry.update();
            }

            lFDrive.setPower(0);
            rFDrive.setPower(0);
            lRDrive.setPower(0);
            rRDrive.setPower(0);

            lFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        lFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (opModeIsActive()) {
            newLeftTarget = lFDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = rFDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

            lFDrive.setTargetPosition(newLeftTarget);
            rFDrive.setTargetPosition(newRightTarget);
            lRDrive.setTargetPosition(newLeftTarget);
            rRDrive.setTargetPosition(newRightTarget);

            lFDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rFDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runTime.reset();
            lFDrive.setPower(Math.abs(speed));
            rFDrive.setPower(Math.abs(speed));
            lRDrive.setPower(Math.abs(speed));
            rRDrive.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runTime.seconds() < timeoutS) &&
                    (lFDrive.isBusy() && rFDrive.isBusy())) {
                telemetry.addData("Path1", "Running to %7d: %7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d", lFDrive.getCurrentPosition(), rFDrive.getCurrentPosition());
                telemetry.update();
            }

            lFDrive.setPower(0);
            rFDrive.setPower(0);
            lRDrive.setPower(0);
            rRDrive.setPower(0);

            lFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

}