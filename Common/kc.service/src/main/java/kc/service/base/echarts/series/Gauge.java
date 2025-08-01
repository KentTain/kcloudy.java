/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package kc.service.base.echarts.series;

import kc.service.base.echarts.Label;
import kc.service.base.echarts.Title;
import kc.service.base.echarts.axis.AxisLine;
import kc.service.base.echarts.axis.AxisTick;
import kc.service.base.echarts.axis.SplitLine;
import kc.service.base.echarts.code.SeriesType;
import kc.service.base.echarts.series.gauge.Detail;
import kc.service.base.echarts.series.gauge.Pointer;
import lombok.Getter;
import lombok.Setter;

/**
 * 仪表盘
 *
 * @author liuzh
 */
@Getter
@Setter
public class Gauge extends Series<Gauge> {
    /**
     * 圆心坐标，支持绝对值（px）和百分比，百分比计算min(width, height) * 50%
     */
    private Object[] center;
    /**
     * 半径，支持绝对值（px）和百分比，百分比计算比，min(width, height) / 2 * 75%，
     * 传数组实现环形图，[内半径，外半径]
     */
    private Object radius;
    /**
     * 开始角度, 饼图（90）、仪表盘（225），有效输入范围：[-360,360]
     */
    private Integer startAngle;
    /**
     * 结束角度,有效输入范围：[-360,360]，保证startAngle - endAngle为正值
     */
    private Integer endAngle;
    /**
     * 指定的最小值
     */
    private Integer min;
    /**
     * 指定的最大值
     */
    private Integer max;
    /**
     * 分割段数，默认为5，为0时为线性渐变，calculable为true是默认均分100份
     */
    private Integer splitNumber;
    /**
     * 坐标轴线，默认显示
     *
     * @see kc.service.base.echarts.series.Line
     */
    private AxisLine axisLine;
    /**
     * 坐标轴小标记，默认显示
     *
     * @see kc.service.base.echarts.axis.AxisTick
     */
    private AxisTick axisTick;
    /**
     * 坐标轴文本标签
     *
     * @see kc.service.base.echarts.Label
     */
    private Label axisLabel;
    /**
     * 主分隔线，默认显示
     *
     * @see kc.service.base.echarts.axis.SplitLine
     */
    private SplitLine splitLine;
    /**
     * 指针样式
     *
     * @see kc.service.base.echarts.series.gauge.Pointer
     */
    private Pointer pointer;
    /**
     * 仪表盘标题
     *
     * @see kc.service.base.echarts.Title
     */
    private Title title;
    /**
     * 仪表盘详情
     *
     * @see kc.service.base.echarts.series.gauge.Detail
     */
    private Detail detail;

    /**
     * 构造函数
     */
    public Gauge() {
        this.type(SeriesType.gauge);
    }

    /**
     * 构造函数,参数:name
     *
     * @param name
     */
    public Gauge(String name) {
        super(name);
        this.type(SeriesType.gauge);
    }

    /**
     * 获取center值
     */
    public Object[] center() {
        return this.center;
    }

    /**
     * 设置center值
     *
     * @param center
     */
    public Gauge center(Object[] center) {
        this.center = center;
        return this;
    }

    /**
     * 获取radius值
     */
    public Object radius() {
        return this.radius;
    }

    /**
     * 设置axisLine值
     *
     * @param axisLine
     */
    public Gauge axisLine(AxisLine axisLine) {
        this.axisLine = axisLine;
        return this;
    }

    /**
     * 设置axisTick值
     *
     * @param axisTick
     */
    public Gauge axisTick(AxisTick axisTick) {
        this.axisTick = axisTick;
        return this;
    }

    /**
     * 设置axisLabel值
     *
     * @param axisLabel
     */
    public Gauge axisLabel(Label axisLabel) {
        this.axisLabel = axisLabel;
        return this;
    }

    /**
     * 设置splitLine值
     *
     * @param splitLine
     */
    public Gauge splitLine(SplitLine splitLine) {
        this.splitLine = splitLine;
        return this;
    }

    /**
     * 设置pointer值
     *
     * @param pointer
     */
    public Gauge pointer(Pointer pointer) {
        this.pointer = pointer;
        return this;
    }

    /**
     * 设置title值
     *
     * @param title
     */
    public Gauge title(Title title) {
        this.title = title;
        return this;
    }

    /**
     * 设置detail值
     *
     * @param detail
     */
    public Gauge detail(Detail detail) {
        this.detail = detail;
        return this;
    }

    /**
     * 圆心坐标，支持绝对值（px）和百分比，百分比计算min(width, height) * 50%
     */
    public Gauge center(Object width, Object height) {
        this.center = new Object[]{width, height};
        return this;
    }

    /**
     * 半径，支持绝对值（px）和百分比，百分比计算比，min(width, height) / 2 * 75%，
     * 传数组实现环形图，[内半径，外半径]
     *
     * @param radius
     * @return
     */
    public Gauge radius(Object radius) {
        this.radius = radius;
        return this;
    }

    /**
     * 半径，支持绝对值（px）和百分比，百分比计算比，min(width, height) / 2 * 75%，
     * 传数组实现环形图，[内半径，外半径]
     *
     * @param width
     * @param height
     * @return
     */
    public Gauge radius(Object width, Object height) {
        radius = new Object[]{width, height};
        return this;
    }

    /**
     * 获取startAngle值
     */
    public Integer startAngle() {
        return this.startAngle;
    }

    /**
     * 设置startAngle值
     *
     * @param startAngle
     */
    public Gauge startAngle(Integer startAngle) {
        this.startAngle = startAngle;
        return this;
    }

    /**
     * 获取endAngle值
     */
    public Integer endAngle() {
        return this.endAngle;
    }

    /**
     * 设置endAngle值
     *
     * @param endAngle
     */
    public Gauge endAngle(Integer endAngle) {
        this.endAngle = endAngle;
        return this;
    }

    /**
     * 获取min值
     */
    public Integer min() {
        return this.min;
    }

    /**
     * 设置min值
     *
     * @param min
     */
    public Gauge min(Integer min) {
        this.min = min;
        return this;
    }

    /**
     * 获取max值
     */
    public Integer max() {
        return this.max;
    }

    /**
     * 设置max值
     *
     * @param max
     */
    public Gauge max(Integer max) {
        this.max = max;
        return this;
    }

    /**
     * 获取splitNumber值
     */
    public Integer splitNumber() {
        return this.splitNumber;
    }

    /**
     * 设置splitNumber值
     *
     * @param splitNumber
     */
    public Gauge splitNumber(Integer splitNumber) {
        this.splitNumber = splitNumber;
        return this;
    }

    /**
     * 坐标轴线，默认显示
     *
     * @see kc.service.base.echarts.series.Line
     */
    public AxisLine axisLine() {
        if (this.axisLine == null) {
            this.axisLine = new AxisLine();
        }
        return this.axisLine;
    }

    /**
     * 坐标轴小标记，默认显示
     *
     * @see kc.service.base.echarts.axis.AxisTick
     */
    public AxisTick axisTick() {
        if (this.axisTick == null) {
            this.axisTick = new AxisTick();
        }
        return this.axisTick;
    }

    /**
     * 坐标轴文本标签
     *
     * @see kc.service.base.echarts.Label
     */
    public Label axisLabel() {
        if (this.axisLabel == null) {
            this.axisLabel = new Label();
        }
        return this.axisLabel;
    }

    /**
     * 主分隔线，默认显示
     *
     * @see kc.service.base.echarts.axis.SplitLine
     */
    public SplitLine splitLine() {
        if (this.splitLine == null) {
            this.splitLine = new SplitLine();
        }
        return this.splitLine;
    }

    /**
     * 指针样式
     *
     * @see kc.service.base.echarts.series.gauge.Pointer
     */
    public Pointer pointer() {
        if (this.pointer == null) {
            this.pointer = new Pointer();
        }
        return this.pointer;
    }

    /**
     * 仪表盘标题
     *
     * @see kc.service.base.echarts.Title
     */
    public Title title() {
        if (this.title == null) {
            this.title = new Title();
        }
        return this.title;
    }

    /**
     * 仪表盘详情
     *
     * @see kc.service.base.echarts.series.gauge.Detail
     */
    public Detail detail() {
        if (this.detail == null) {
            this.detail = new Detail();
        }
        return this.detail;
    }
}
