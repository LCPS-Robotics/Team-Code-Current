package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Gold Auto", group="14465")
public class AutonClimbing extends LinearOpMode {

    private DcMotor climber = null;

    //lFDrive Motors
    private DcMotor lFDrive = null;
    private DcMotor lRDrive = null;

    //rFDrive Motors
    private DcMotor rFDrive = null;
    private DcMotor rRDrive = null;

    //Arm Motors
    private Servo arm1 = null;
    private Servo arm2 = null;

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

        arm1 = hardwareMap.get(Servo.class, "Arm1");
        arm2 = hardwareMap.get(Servo.class, "Arm2");

        climber = hardwareMap.get(DcMotor.class, "Climber");

        arm2.setDirection(Servo.Direction.REVERSE);

        rFDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rRDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");

        climber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rRDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path0", "Starting at %7d : %7d", lFDrive.getCurrentPosition(), rFDrive.getCurrentPosition());
        telemetry.update();

        waitForStart();

        mecanumDrive(1.0, 36,  5.0);
    }

    public void climbDown(int targetDistance, double speed, double timeoutS) {
        if (opModeIsActive()){
            climber.setTargetPosition(targetDistance);

            climber.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runTime.reset();
            climber.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runTime.seconds() < timeoutS) &&
                    (climber.isBusy())) {
                telemetry.addData("Path2", "Climbing at %7d", climber.getCurrentPosition());
                telemetry.update();
            }

            climber.setPower(0);

            climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
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
