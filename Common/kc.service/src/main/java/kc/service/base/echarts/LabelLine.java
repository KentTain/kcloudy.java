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

package kc.service.base.echarts;

import kc.service.base.echarts.style.LineStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 标签视觉引导线l
 *
 * @author liuzh
 */
@Getter
@Setter
public class LabelLine implements Serializable {

    private static final long serialVersionUID = 3359589478018844177L;

    /**
     * 饼图标签视觉引导线显示策略，可选为：true（显示） | false（隐藏）
     */
    private Boolean show;
    /**
     * 线长 ，从外圆边缘起计算，可为负值
     */
    private Integer length;
    /**
     * 线条样式，详见lineStyle
     *
     * @see kc.service.base.echarts.style.LineStyle
     */
    private LineStyle lineStyle;

    /**
     * 获取show值
     */
    public Boolean show() {
        return this.show;
    }

    /**
     * 设置show值
     *
     * @param show
     */
    public LabelLine show(Boolean show) {
        this.show = show;
        return this;
    }

    /**
     * 获取length值
     */
    public Integer length() {
        return this.length;
    }

    /**
     * 设置length值
     *
     * @param length
     */
    public LabelLine length(Integer length) {
        this.length = length;
        return this;
    }

    /**
     * 线条样式，详见lineStyle
     *
     * @see kc.service.base.echarts.style.LineStyle
     */
    public LineStyle lineStyle() {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        return this.lineStyle;
    }

    /**
     * 设置style值
     *
     * @param style
     */
    public LabelLine lineStyle(LineStyle style) {
        this.lineStyle = style;
        return this;
    }
}
