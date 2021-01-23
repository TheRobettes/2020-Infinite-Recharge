/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * An example command that uses an example subsystem.
 */
public abstract class JoystickCommands extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  protected static final Joystick joystickXbox = new Joystick(0);
  protected static final Joystick joystickAttack = new Joystick(1);

  public enum JoystickType {
  XBox(0), Attack(1);

  public final int value;

		JoystickType(int value) {
			this.value = value;
    };
  }

public static void whileCommand(
    JoystickType option, int buttonNumber, Command buttonAction) {
    Joystick humanID = (option == JoystickType.XBox)
      ? joystickXbox
      : joystickAttack;

      new JoystickButton(humanID, buttonNumber).whileHeld(buttonAction);
  }

  public static void whenPressedCommand(
    JoystickType option, int buttonNumber, Command buttonAction) {
    Joystick humanID = (option == JoystickType.XBox)
      ? joystickXbox
      : joystickAttack;

      new JoystickButton(humanID, buttonNumber).whenPressed(buttonAction);
  }


}

