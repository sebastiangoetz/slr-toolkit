package wizard;

import helloworldchart.ChartGenerator;
import helloworldchart.ICommunicationView;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ChartWizard extends Wizard {

	private final String chartViewId = "chart.view.helloworld";

	protected FirstPage one;
	// protected SecondPage two;

	private Chart myChart;

	public ChartWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Create a Chart";
	}

	@Override
	public void addPages() {
		one = new FirstPage();
		addPage(one);
		// two = new SecondPage();
		// addPage(two);

	}

	@Override
	public boolean performFinish() {

		// here the chart is passed to the view.

		if (one.getButton1().getSelection()) {
			myChart = ChartGenerator.createBar();
		} else if (one.getButton2().getSelection()) {
			myChart = ChartGenerator.createBubble("A1", "A2", "Langes Wort",
					"Birne");
		}
		ICommunicationView view = null;
		try {
			view = (ICommunicationView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.showView(chartViewId);
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		view.setChart(myChart);
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().showView(chartViewId);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return true;
	}
}
