package github.mikephil.charting.interfaces.dataprovider;

import github.mikephil.charting.components.YAxis;
import github.mikephil.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
