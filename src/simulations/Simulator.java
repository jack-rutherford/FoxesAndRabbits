package simulations;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;


/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * rabbits and foxes.
 * 
 *
 * @author jackrutherford
 * @date 1/28/22
 * @class CSCI 235
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Simulator extends JFrame
{
	// Constants representing configuration information for the simulation.
	// The default width for the grid.
	private static final int DEFAULT_WIDTH = 120;
	// The default depth of the grid.
	private static final int DEFAULT_DEPTH = 80;

	// Lists of animals in the field.
	private List<Animal> animals;

	// The current step of the simulation.
	private int step;
	// The population generator to control the population
	private PopulationGenerator popGen;
	// The field for where the animals are
	private Field field;

	// All of the JPanel Components
	private JPanel panel;
	private JPanel cp;
	private JLabel label1;
	private JLabel label2;
	private JButton innerButton1;
	private JButton innerButton2;
	private JTextField textField;
	private final int WINDOW_WIDTH = 250, WINDOW_HEIGHT = 350;


	/**
	 * Construct a simulation field with default size.
	 */
	public Simulator()
	{

		this(DEFAULT_DEPTH, DEFAULT_WIDTH);
		popGen = new PopulationGenerator();
		field = new Field(DEFAULT_DEPTH, DEFAULT_WIDTH);
		animals = new ArrayList<>();
		popGen.setColors(DEFAULT_DEPTH, DEFAULT_WIDTH);

		reset();

		setTitle("Simulator Stepper");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildPanel();
		add(panel);
		setVisible(true);
	}

	/**
	 * Create a simulation field with the given size.
	 * @param depth Depth of the field. Must be greater than zero.
	 * @param width Width of the field. Must be greater than zero.
	 */
	public Simulator(int depth, int width)
	{
		if(width <= 0 || depth <= 0) {
			System.out.println("The dimensions must be >= zero.");
			System.out.println("Using default values.");
			depth = DEFAULT_DEPTH;
			width = DEFAULT_WIDTH;
		}
		popGen = new PopulationGenerator();
		field = new Field(depth, width);
		animals = new ArrayList<>();
		popGen.setColors(depth, width);

		reset();

		setTitle("Simulator Stepper");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildPanel();
		add(panel);
		setVisible(true);
	}

	/**
	 * Build the panel, and make all of the objects necessary for the panel 
	 */
	private void buildPanel() {

		// Make JPanel buttons and button group & label
		label1 = new JLabel("Choose # of Steps");
		JButton b1 = new JButton("1 Step"); //add tool tips to these
		JButton b2 = new JButton("100 Steps");
		JButton b3 = new JButton("Long Simulation (4000 Steps)");
		JButton b4 = new JButton("Custom # of Steps");

		//assign action listeners to each button
		b1.addActionListener(new ActionListener1());
		b2.addActionListener(new ActionListener2());
		b3.addActionListener(new ActionListener3());
		b4.addActionListener(new ActionListener4());

		// Make panel and add components to the panel (box layout)
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(label1);
		panel.add(Box.createVerticalStrut(20));
		panel.add(b1);
		panel.add(b2);
		panel.add(b3);
		panel.add(b4);

		//make a small border for the panel
		panel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));

		// Align the buttons to be centered
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
		b1.setAlignmentX(Component.CENTER_ALIGNMENT);
		b2.setAlignmentX(Component.CENTER_ALIGNMENT);
		b3.setAlignmentX(Component.CENTER_ALIGNMENT);
		b4.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	/**
	 * Event listener class for "1 Step" b1 button
	 */
	private class ActionListener1 implements ActionListener{

		/**
		 * Override method to record the action performed
		 * Simulates a single step for b1
		 * 
		 * @param ActionEvent e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			simulateOneStep();
		}
	}

	/**
	 * Event listener class for "100 Step" b2 button
	 */
	private class ActionListener2 implements ActionListener{

		/**
		 * Override method to record the action performed
		 * Simulates 100 steps for b2
		 * 
		 * @param ActionEvent e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			simulate(100);
		}
	}

	/**
	 * Event listener class for "Long Simulation" b3 button
	 */
	private class ActionListener3 implements ActionListener{

		/**
		 * Override method to record the action performed
		 * Simulates a long simulation (4000 steps) for b3
		 * 
		 * @param ActionEvent e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			runLongSimulation();
		}
	}

	/**
	 * Event listener class for "Custom # of Steps" b4 button
	 * Makes a new panel with more buttons and a text field
	 */
	private class ActionListener4 implements ActionListener{

		/**
		 * Override method to record the action performed
		 * Sets up components for new panel and gives the user the ]
		 * option to input a custom number of steps
		 * 
		 * Makes the old panel not visible, makes the components and new panel
		 * and then makes that visible. Includes more action listeners for the
		 * new buttons
		 * 
		 * 
		 * @param ActionEvent e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			panel.setVisible(false); // Sets original panel to not be visible

			// If cp is already created, just make it visible instead of making a new one
			if(cp != null) {
				cp.setVisible(true);
				return;
			}

			// New panel with flow layout
			// I'm not sure why I named it cp but I know I had a good reason at one point :L
			cp = new JPanel();
			cp.setLayout(new FlowLayout());

			// Make all components for new panel
			textField = new JTextField(10);
			label2 = new JLabel("Custom # of Steps");
			innerButton1 = new JButton("Simulate");
			innerButton2 = new JButton("Go Back");
			innerButton1.addActionListener(new InnerActionListener1()); // simulates based on user text input
			innerButton2.addActionListener(new InnerActionListener2()); // goes back to other screen

			//make a small border for the cp panel
			cp.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

			// Add components to new panel
			cp.add(label2);
			cp.add(textField);
			cp.add(innerButton1);
			cp.add(innerButton2);
			add(cp); //add cp panel to the main panel (I don't really know why this works)
			cp.setVisible(true);
		}
	}

	/**
	 * Event listener class for inner panel (action for innerbutton1)
	 */
	private class InnerActionListener1 implements ActionListener{

		/**
		 * Override method to record the action performed
		 * For the text field to make sure a numeric number is input
		 * and simulate the number of steps input by the user
		 * 
		 * @param ActionEvent e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			String userInput = textField.getText();

			if(!userInput.matches("[0-9]+")) { //check if the input String is only numeric
				JOptionPane.showMessageDialog(cp, "Please enter a number");
				return;
			}

			int temp = Integer.parseInt(userInput); //turn the input into an integer
			simulate(temp);

		}
	}

	/**
	 * Event listener class for inner panel (action for innerbutton2)
	 */
	private class InnerActionListener2 implements ActionListener{

		/**
		 * Override method to record the action performed
		 * Sets the inner cp panel to not visible, and original panel to visible
		 * 
		 * @param ActionEvent e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			cp.setVisible(false);
			panel.setVisible(true);
		}
	}

	/**
	 * Run the simulation from its current state for a reasonably long 
	 * period (4000 steps).
	 */
	public void runLongSimulation()
	{
		simulate(4000);
	}

	/**
	 * Run the simulation for the given number of steps.
	 * Stop before the given number of steps if it ceases to be viable.
	 * @param numSteps The number of steps to run for.
	 */
	public void simulate(int numSteps)
	{
		for(int step=1; step <= numSteps && popGen.getSimulatorView().isViable(field); step++) {
			simulateOneStep();
			//delay(100);   // uncomment this to run more slowly
		}
	}

	/**
	 * Run the simulation from its current state for a single step. Iterate
	 * over the whole field updating the state of each fox and rabbit.
	 */
	public void simulateOneStep()
	{
		step++;

		// Provide space for newborn rabbits.
		List<Animal> newAnimals = new ArrayList<>();        
		// Let all rabbits act.

		for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
			Animal animal = it.next();
			animal.act(newAnimals);
			if(!animal.isAlive()) {
				it.remove();
			}
		}
		animals.addAll(newAnimals);
		popGen.getSimulatorView().showStatus(step, field);
	}

	/**
	 * Reset the simulation to a starting position.
	 */
	public void reset()
	{
		step = 0;
		animals.clear();
		popGen.populate(field, animals);

		// Show the starting state in the view.
		popGen.getSimulatorView().showStatus(step, field);
	}

	/**
	 * Pause for a given time.
	 * @param millisec  The time to pause for, in milliseconds
	 */
	private void delay(int millisec)
	{
		try {
			Thread.sleep(millisec);
		}
		catch (InterruptedException ie) {
			// wake up
		}
	}

	/**
	 * Classes imported from OFWJ resources and main method included to 
	 * enable execution in Eclipse.
	 * 
	 * Date: 8-27-20
	 * @param args  Arguments for main method..
	 */
	public static void main(String[] args) {
		//startSimulation();
		Simulator simulate = new Simulator(80,120);
//		  simulate.simulate(62290);
	}

}
