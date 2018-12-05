package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class PracticeBotCode extends OpMode {

    /*
    Data Set 1
        Red: 175
        Green: 95
        Blue: 68
        HUE: 34.5
        Distance: 5.45 CM

    Data Set 2
        Red: 78
        Green: 60
        Blue: 35
        HUE: 35
        Distance: 6.5 CM

    Data Set 3
        Red: 50
        Green: 40
        Blue: 24
        HUE: 36
        Distance: 8.5 CM

     */

    private ElapsedTime runTime = new ElapsedTime();

    private boolean driveTank = true;
    double toggleTime;

    private Servo bucket = null;

    //leftDrive Motors
    private DcMotor lFDrive = null;
    private DcMotor lRDrive = null;

    //rightDrive Motors
    private DcMotor rFDrive = null;
    private DcMotor rRDrive = null;

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        bucket = hardwareMap.get(Servo.class, "bucketServo");
        lFDrive = hardwareMap.get(DcMotor.class, "LFDrive");
        lRDrive = hardwareMap.get(DcMotor.class, "LRDrive");
        rFDrive = hardwareMap.get(DcMotor.class, "RFDrive");
        rRDrive = hardwareMap.get(DcMotor.class, "RRDrive");

        rFDrive.setDirection(DcMotor.Direction.REVERSE);
        rRDrive.setDirection(DcMotor.Direction.REVERSE);

        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

    }

    @Override
    public void start() {
        runTime.reset();
        toggleTime = runTime.seconds();
        bucket.setPosition(.25);
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

        telemetry.addData("Servo Angle", bucket.getPosition());

        if(gamepad1.right_trigger >= .5){
            bucket.setPosition(.25);
        }

        if(gamepad1.left_trigger >= .5){
            bucket.setPosition(1);
        }

        if(gamepad1.b){
            sensorColor.enableLed(false);
        }

        if(gamepad1.a){
            sensorColor.enableLed(true);
        }

        telemetry.update();
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