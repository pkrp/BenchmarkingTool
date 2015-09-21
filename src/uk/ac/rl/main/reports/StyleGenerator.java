package uk.ac.rl.main.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

public class StyleGenerator {

	public static StyleBuilder getBoldStyle(StyleBuilder style) {
		return stl.style(style).bold();
	}

	public static StyleBuilder getHorizontalAlignmentStyle(StyleBuilder style, HorizontalAlignment type) {
		return stl.style(style).setHorizontalAlignment(type);
	}

	public static StyleBuilder getColumnTitleStyle() {
		StyleBuilder boldCenteredStyle = getHorizontalAlignmentStyle(getBoldStyle(stl.style()),
				HorizontalAlignment.CENTER);
		return stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(new Color(210, 240, 234))
				.setFontSize(12);
	}

	public static StyleBuilder getTitleStyle() {
		return getHorizontalAlignmentStyle(getBoldStyle(stl.style()), HorizontalAlignment.CENTER).setFontSize(16)
				.setForegroundColor(new Color(58, 84, 79));
	}

	public static StyleBuilder getTimeVariableStyle() {
		return getHorizontalAlignmentStyle(
				getBoldStyle(stl.style()).setBorder(stl.pen1Point()).setBackgroundColor(new Color(210, 240, 234)),
				HorizontalAlignment.LEFT);
	}

	public static StyleBuilder getSizeVariableStyle() {
		return getHorizontalAlignmentStyle(stl.style().setBorder(stl.penThin()), HorizontalAlignment.LEFT);
	}
}
