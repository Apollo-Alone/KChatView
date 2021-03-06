package com.ex.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ex.chat.compat.ChartConstraintSet;
import com.ex.chat.drawing.AxisDrawing;
import com.ex.chat.drawing.AxisExtremumDrawing;
import com.ex.chat.drawing.BOLLDrawing;
import com.ex.chat.drawing.CursorDrawing;
import com.ex.chat.drawing.GridDrawing;
import com.ex.chat.drawing.HighlightDrawing;
import com.ex.chat.drawing.IndicatorsLabelDrawing;
import com.ex.chat.drawing.KDJDrawing;
import com.ex.chat.drawing.MACDDrawing;
import com.ex.chat.drawing.MADrawing;
import com.ex.chat.drawing.RSIDrawing;
import com.ex.chat.drawing.VolumeDrawing;
import com.ex.chat.drawing.candle.CandleDrawing;
import com.ex.chat.drawing.candle.CandleSelectorDrawing;
import com.ex.chat.drawing.depth.DepthDrawing;
import com.ex.chat.drawing.depth.DepthGridDrawing;
import com.ex.chat.drawing.depth.DepthHighlightDrawing;
import com.ex.chat.drawing.depth.DepthSelectorDrawing;
import com.ex.chat.drawing.timeLine.TimeLineDrawing;
import com.ex.chat.entry.ChartEntry;
import com.ex.chat.enumeration.DataDisplayType;
import com.ex.chat.enumeration.ModuleType;
import com.ex.chat.enumeration.RenderModel;
import com.ex.chat.marker.AxisTextMarker;
import com.ex.chat.marker.GridTextMarker;
import com.ex.chat.module.BOLLChartModule;
import com.ex.chat.module.CandleChartModule;
import com.ex.chat.module.DepthChartModule;
import com.ex.chat.module.KDJChartModule;
import com.ex.chat.module.MACDChartModule;
import com.ex.chat.module.RSIChartModule;
import com.ex.chat.module.TimeLineChartModule;
import com.ex.chat.module.VolumeChartModule;
import com.ex.chat.module.base.AbsChartModule;
import com.ex.chat.module.base.AuxiliaryChartModule;
import com.ex.chat.module.base.MainChartModule;
import com.ex.chat.render.AbsRender;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author wancheng
 * date   2020/4/29
 * desc  The code can't block the young lady!
 * version  v1.0
 */
public class ChartLayout extends ConstraintLayout {
    private static final String TAG = "ChartLayout";

    private ChartEntry chartEntry;

    private LinkedHashMap<ModuleType, AbsChartModule> ChartModules;

    private List<ModuleType> enableModuleTypes;

    private DataDisplayType dataDisplayType;

    public ChartLayout(Context context) {
        this(context, null);
    }

    public ChartLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
        initChartModules();
        initChart();
    }

    /**
     * ?????????
     */
    private void init() {
        this.chartEntry = new ChartEntry();
        this.ChartModules = new LinkedHashMap<>();
        this.enableModuleTypes = new ArrayList<>();
    }

    /**
     * ??????????????????
     */
    private void initChartModules() {
        this.ChartModules.clear();
        CandleChartModule candleModule = new CandleChartModule();
        candleModule.addDrawing(new CandleDrawing());//???????????????
        candleModule.addDrawing(new MADrawing());//???????????????
        candleModule.addDrawing(new AxisDrawing());//x?????????
        candleModule.addDrawing(new IndicatorsLabelDrawing());//????????????????????????
        candleModule.addDrawing(new AxisExtremumDrawing());//Y???????????????
        //candleIndex.addDrawing(new EmptyDataDrawing());//???????????????
        candleModule.setEnable(false);
        this.ChartModules.put(candleModule.getModuleType(), candleModule);

        TimeLineChartModule timeLineModule = new TimeLineChartModule();
        timeLineModule.addDrawing(new TimeLineDrawing());//???????????????
        timeLineModule.addDrawing(new AxisDrawing());//x?????????
        timeLineModule.addDrawing(new AxisExtremumDrawing());//Y???????????????
        timeLineModule.setEnable(false);
        this.ChartModules.put(timeLineModule.getModuleType(), timeLineModule);

        VolumeChartModule volumeModule = new VolumeChartModule();
        volumeModule.addDrawing(new VolumeDrawing());
        volumeModule.addDrawing(new MADrawing());
        volumeModule.addDrawing(new IndicatorsLabelDrawing());
        volumeModule.addDrawing(new AxisExtremumDrawing());
        this.ChartModules.put(volumeModule.getModuleType(), volumeModule);

        MACDChartModule macdModule = new MACDChartModule();
        macdModule.addDrawing(new MACDDrawing());
        macdModule.addDrawing(new AxisExtremumDrawing());
        macdModule.setEnable(false);
        this.ChartModules.put(macdModule.getModuleType(), macdModule);

        RSIChartModule rsiModule = new RSIChartModule();
        rsiModule.addDrawing(new RSIDrawing());
        rsiModule.addDrawing(new AxisExtremumDrawing());
        rsiModule.setEnable(false);
        this.ChartModules.put(rsiModule.getModuleType(), rsiModule);

        KDJChartModule kdjModule = new KDJChartModule();
        kdjModule.addDrawing(new KDJDrawing());
        kdjModule.addDrawing(new AxisExtremumDrawing());
        kdjModule.setEnable(false);
        this.ChartModules.put(kdjModule.getModuleType(), kdjModule);

        BOLLChartModule bollModule = new BOLLChartModule();
        bollModule.addDrawing(new BOLLDrawing());
        bollModule.addDrawing(new AxisExtremumDrawing());
        bollModule.setEnable(false);
        this.ChartModules.put(bollModule.getModuleType(), bollModule);
    }

    /**
     * ????????? ??????
     */
    private void initChart() {
        for (int i = 0, z = getChildCount(); i < z; i++) {
            View view = getChildAt(i);
            if (!(view instanceof Chart)) {
                continue;
            }
            Chart chart = (Chart) view;
            AbsRender reader = chart.getRender();
            switch (chart.getRenderModel()) {
                case CANDLE://?????????
                    HighlightDrawing candleHighlight = new HighlightDrawing();
                    candleHighlight.addMarkerView(new GridTextMarker());
                    candleHighlight.addMarkerView(new AxisTextMarker());
                    reader.addFloatDrawing(candleHighlight);
                    reader.addFloatDrawing(new GridDrawing());
                    reader.addFloatDrawing(new CandleSelectorDrawing());
                    for (Map.Entry<ModuleType, AbsChartModule> item : ChartModules.entrySet()) {
                        reader.addChartModule(item.getValue());
                    }
                    break;
                case DEPTH://?????????
                    DepthChartModule depthChartModule = new DepthChartModule();
                    depthChartModule.addDrawing(new AxisDrawing());//x?????????
                    depthChartModule.addDrawing(new DepthGridDrawing());//Y?????????
                    depthChartModule.addDrawing(new DepthDrawing());//???????????????
                    DepthHighlightDrawing depthHighlight = new DepthHighlightDrawing();
                    depthHighlight.addMarkerView(new GridTextMarker());
                    depthHighlight.addMarkerView(new AxisTextMarker());
                    reader.addChartModule(depthChartModule);
                    reader.addFloatDrawing(depthHighlight);
                    reader.addFloatDrawing(new DepthSelectorDrawing());
                    chart.setEnableRightRefresh(false);
                    chart.setEnableLeftRefresh(false);
                    break;
            }
            this.chartEntry.setChart(chart);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param moduleType ??????
     */
    public boolean switchModuleType(ModuleType moduleType) {
        if (!ChartModules.containsKey(moduleType)) {
            return false;
        }
        this.enableModuleTypes.clear();
        AbsChartModule chartModule = ChartModules.get(moduleType);
        if (null == chartModule || chartModule.isEnable()) {
            return false;
        }
        chartModule.setEnable(true);
        Class classType = chartModule instanceof MainChartModule ?
                MainChartModule.class : AuxiliaryChartModule.class;
        for (Map.Entry<ModuleType, AbsChartModule> item : ChartModules.entrySet()) {
            if (item.getValue().isEnable() && classType.isInstance(item.getValue())
                    && item.getKey() != moduleType && item.getKey() != ModuleType.VOLUME) {
                item.getValue().setEnable(false);
            } else if (item.getValue().isEnable() && item.getKey() != ModuleType.VOLUME) {
                //?????????????????????????????????
                this.enableModuleTypes.add(item.getKey());
            }
        }
        return true;
    }

    /**
     * ?????????????????????????????????
     *
     * @return List<ModuleType> ???????????????????????????????????????
     */
    public List<ModuleType> getEnableModuleTypes() {
        return enableModuleTypes;
    }

    /**
     * ?????????????????????????????????1:???????????????2:???????????????
     */
    public void setDataDisplayType(DataDisplayType type) {
        if (type == dataDisplayType) {
            return;
        }
        this.dataDisplayType = type;
        for (int i = 0, z = getChildCount(); i < z; i++) {
            View view = getChildAt(i);
            if (view instanceof Chart) {
                Chart chart = (Chart) view;
                if (chart.getRenderModel() == RenderModel.CANDLE) {
                    AbsRender reader = chart.getRender();
                    if (type == DataDisplayType.REAL_TIME) {
                        chart.setEnableRightRefresh(false);//????????????
                        reader.getAttribute().rightScrollOffset = 250;  //????????????????????????
                        chart.getRender().getChartModule(ModuleType.CANDLE).addDrawing(new CursorDrawing());
                        chart.getRender().getChartModule(ModuleType.TIME).addDrawing(new CursorDrawing());
                    } else {
                        chart.setEnableRightRefresh(true);//????????????
                        reader.getAttribute().rightScrollOffset = 0;  //????????????????????????
                        chart.getRender().getChartModule(ModuleType.CANDLE).removeDrawing(CursorDrawing.class);
                        chart.getRender().getChartModule(ModuleType.TIME).removeDrawing(CursorDrawing.class);
                    }
                    break;
                }
            }
        }
    }

    public void setConstraintSet(ChartConstraintSet set) {
        set.applyTo(this);
    }
}
