package github.mikephil.charting.interfaces.dataprovider;

import github.mikephil.charting.components.YAxis.AxisDependency;
import github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import github.mikephil.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
