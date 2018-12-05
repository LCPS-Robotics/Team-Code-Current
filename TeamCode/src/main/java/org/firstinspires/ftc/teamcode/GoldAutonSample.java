package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

public class GoldAutonSample extends LinearOpMode {

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;
    boolean colorSpotted = false;
    Servo kicker = null;
    Servo dump = null;

    @Override
    public void runOpMode() throws InterruptedException {
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

        kicker = hardwareMap.get(Servo.class, "Kicker");
        dump = hardwareMap.get(Servo.class, "Dump");

        waitForStart();

        //Use color Check in between the driving pattern at sample. If color is spotted then move forward X inches where is the distance from color sensor to servo.
        //Below line fires kicker at 180 deg
        if (colorSpotted) {
            //Drive forward x inches then kick
            servoFire(180, false, true);
        }

        //to dump marker use servo fire again at correct section.
        servoFire(180, true, false);

    }

    public void servoFire(double angle, boolean dumper, boolean kick){
        if (dumper){
            dump.setPosition(angle);
        }

        if(kick){
            kicker.setPosition(angle);
        }
    }

    public void ColorCheck() {
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        while (opModeIsActive()) {
            // convert the RGB values to HSV values.
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            telemetry.update();

            //set values to match color gradient of cube

            if (sensorColor.red() >= 100 && sensorColor.green() >= 100 && sensorColor.blue() >= 100){
                colorSpotted = true;
            }
            else{
                colorSpotted = false;
            }
        }

    }
}

