package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

// Created by Michael Pate in November 2017 for FTC 2836
// Thanks to Devin for help

@TeleOp(name="2836TP210", group="Teleop")
public class Team2836_TP210 extends LinearOpMode {
    // Used for teleop telemetry
    private ElapsedTime runtime = new ElapsedTime();

    // Motors on the robot
    private DcMotor leftFront  = null;
    private DcMotor leftRear   = null;
    private DcMotor rightFront = null;
    private DcMotor rightRear  = null;
    private DcMotor mechArm    = null;
    private DcMotor leftWheel  = null;
    private DcMotor rightWheel = null;

    @Override
    public void runOpMode() throws InterruptedException {
        // Map the motors to their actual selves
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear   = hardwareMap.get(DcMotor.class, "leftRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightRear  = hardwareMap.get(DcMotor.class, "rightRear");
        mechArm    = hardwareMap.get(DcMotor.class, "mechLift"); //kept named as mechLift in-app for legacy
        rightWheel = hardwareMap.get(DcMotor.class, "rightWheel");
        leftWheel  = hardwareMap.get(DcMotor.class, "leftWheel");

        // All motors initialized forward so that 1 = clockwise and -1 = counterclockwise
        mechArm    .setDirection(DcMotor.Direction.FORWARD);
        leftWheel  .setDirection(DcMotor.Direction.FORWARD);
        rightWheel .setDirection(DcMotor.Direction.FORWARD);
        leftFront  .setDirection(DcMotor.Direction.FORWARD);
        leftRear   .setDirection(DcMotor.Direction.FORWARD);
        rightFront .setDirection(DcMotor.Direction.FORWARD);
        rightRear  .setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // MECHANISM MOVEMENT //////////////////////////////////////////////////////////////////
                boolean armUp           =  gamepad2.dpad_up;
                boolean armDown         =  gamepad2.dpad_down;
                double  leftWheelPower  = -gamepad2.left_stick_y;
                double  rightWheelPower = -gamepad2.right_stick_y;

                // Arm mechanism
                if (armUp) mechArm.setPower(1);
                else if (armDown) mechArm.setPower(-1);
                else mechArm.setPower(0);

                //Spinning wheel mechanism
                leftWheel.setPower(leftWheelPower);
                rightWheel.setPower(rightWheelPower);

            // ROBOT MOVEMENT //////////////////////////////////////////////////////////////////////
                double driveX        =  gamepad1.left_stick_x;
                double driveY        = -gamepad1.left_stick_y;
                double leftRotation  = -(gamepad1.left_trigger - 1.0);
                double rightRotation = -(gamepad1.right_trigger - 1.0);
                double mp[]          =  {0.0, 0.0, 0.0, 0.0};

                // Motor power algorithm
                mp[0] = (-driveY +  driveX) + (rightRotation - leftRotation); //m1
                mp[1] = ( driveY + -driveX) + (rightRotation - leftRotation); //m2
                mp[2] = ( driveY +  driveX) + (rightRotation - leftRotation); //m3
                mp[3] = (-driveY + -driveX) + (rightRotation - leftRotation); //m4

                // Top motor power range is -1 to 1
                mp[0] = Range.clip(mp[0], -1, 1);
                mp[1] = Range.clip(mp[1], -1, 1);
                mp[2] = Range.clip(mp[2], -1, 1);
                mp[3] = Range.clip(mp[3], -1, 1);

                // Update motor powers
                leftFront .setPower(mp[0]);
                leftRear  .setPower(mp[3]);
                rightFront.setPower(mp[1]);
                rightRear .setPower(mp[2]);

            // TELEMETRY UPDATES ///////////////////////////////////////////////////////////////////
                telemetry .addData("Motor Powers:", " m1: %.2f, m2: %.2f, m3: %.2f, m4: %.2f", mp[0], mp[1], mp[2], mp[3]);
                telemetry .addData("Joystick Coords:", " %.2f, %.2f", driveX, driveY);
                telemetry .update();
        }
    }
}
