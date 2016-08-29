package com.eli.calc.shape.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.eli.calc.shape.config.AppConfig;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.ShapeCalculatorService;
import com.eli.shape.calc.reports.ShapeCalculationsReports;

public class SimpleComandLineMenuDrivenShapeCalcApp {

	private static final Logger logger = LoggerFactory.getLogger(SimpleComandLineMenuDrivenShapeCalcApp.class);

	private final static String continueMessage = "Press <ENTER> to continue with sample usage:";
	
	private final static String finalReportMessage = "Note: You can look for FINAL REPORT at end of run";
	
	
	private final static String userInputMessage = 
							"This sample is controlled by user input\n";
	
	private static String shapes = "";
	static {
		int i=1;
		for (ShapeName shapeName : ShapeName.values()) {
			shapes+=""+(i++)+") " + shapeName.name() + "\n";
		};
	}
	private final static String selectShapeMenu = "\n\nSelect Shape:\n"
												+ "-----------------------------\n"
												+ shapes
												+ "Q: Quit\n"
												+ "-----------------------------\n"
												+ "Enter number:";


	private static String types = "";
	static {
		int i=1;
		for (CalcType calcType : CalcType.values()) {
			types+=""+(i++)+") " + calcType.name() + "\n";
		};
	}
	private final static String selectTypeMenu = "\n\nSelect Calculation:\n"
												+ "-----------------------------\n"
												+ types
												+ "Q: Quit\n"
												+ "-----------------------------\n"
												+ "Enter number:";


	private final static String enterDimMenu = "\n\nEnter a dimenion:";

	private ShapeCalculatorService calculator;
	
	private final BufferedReader in;

	SimpleComandLineMenuDrivenShapeCalcApp(BufferedReader in) {

		this.in = in;

		ApplicationContext  ctx = new AnnotationConfigApplicationContext(AppConfig.class);


		calculator = ctx.getBean(ShapeCalculatorService.class);     //by the interface
		
	}

	public void doIt() {

		try {
			ShapeName shapeName = selectShapeName();
	
			CalcType type = selectCalcType();

			double dimension = enterDimension();
		
			calculator.queueCalculationRequest(shapeName, type, dimension);
		
			System.out.println("\n\n\nFINAL REPORT========================================================\n");
			int calcsRun = calculator.runAllPendingRequestsStopOnError();
			System.out.println(ShapeCalculationsReports.formattedResultsReportByShapeByDimension(calculator.getAllCalculationResults()));
			System.out.println(
					ShapeCalculationsReports.formattedResultsReportSummary(calculator.getAllCalculationResults())
					+ "\n"
					+ "calcsRun = " + calcsRun
					);
		} catch (IllegalArgumentException e)  { }
	}
	

	private ShapeName selectShapeName() {
		
		String line = "";
		
		for (;;) {

			System.out.print(selectShapeMenu);

			try {
				line=this.in.readLine();
			} catch (IOException e) {
				logger.debug(e.getMessage(),e);
			}

			if ("1".equals(line)) {
				return ShapeName.CIRCLE;
			} else if ("2".equals(line)) {
				return ShapeName.SQUARE;
			} else if ("3".equals(line)) {
				return ShapeName.EQUILATERALTRIANGLE;
			} else if ("4".equals(line)) {
				return ShapeName.SPHERE;
			} else if ("5".equals(line)) {
				return ShapeName.CUBE;
			} else if ("6".equals(line)) {
				return ShapeName.TETRAHEDRON;
			} else if ("Q".toLowerCase().equals(line.toLowerCase())) {
				System.exit(0);
			}
		}
	}
	
	private CalcType selectCalcType() {
		
		String line = "";
		
		for (;;) {

			System.out.print(selectTypeMenu);

			try {
				line=this.in.readLine();
			} catch (IOException e) {
				logger.debug(e.getMessage(),e);
			}

			if ("1".equals(line)) {
				return CalcType.CALC_AREA;
			} else if ("2".equals(line)) {
				return CalcType.CALC_VOLUME;
			} else if ("3".equals(line)) {
				return CalcType.CALC_FOO;
			} else if ("4".equals(line)) {
				return CalcType.CALC_FOOBAR;
			} else if ("5".equals(line)) {

			} else if ("6".equals(line)) {

			} else if ("Q".toLowerCase().equals(line.toLowerCase())) {
				System.exit(0);
			}
		}
	}

	private double enterDimension() {
		
		String line = "";
		
		double dimension = 0;
		
		for (;;) {

			System.out.print(enterDimMenu);

			try {
				line=this.in.readLine();
			} catch (IOException e) {
				logger.debug(e.getMessage(),e);
			}

			try {
				dimension = Double.parseDouble(line);
				return dimension;
			} catch (NumberFormatException e) { }

		}
	}	
	
	
	public static void main(String[] args) throws Exception {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		SimpleComandLineMenuDrivenShapeCalcApp app = new SimpleComandLineMenuDrivenShapeCalcApp(in);
		
		System.out.println(userInputMessage);
		System.out.println();
		System.out.println(finalReportMessage);
		System.out.println();
		System.out.println(continueMessage);
		in.readLine();

		while (1==1) {
			app.doIt();
		}

	}

}
