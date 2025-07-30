//日期对比
let CompareUtil = {
    /*
     * 比较两个日期的大小
     * 传入的参数推荐是"yyyy-mm-dd"的格式，其他的日期格式也可以，但要保证一致
     */
    dateCompare: function (date1, date2) {
        if (date1 && date2) {
            let a = new Date(date1);
            let b = new Date(date2);
            return a <= b;
        }
    },
    /*
     * @author : Kent
     * @desc: 比较两个时间的大小（传入的参数是"HH:mm"的格式，）
     * @param: time1:目标时间;time2:被比较时间
     */
    timeCompare: function (time1, time2) {
        //console.info(time1+"-"+time2);
        try {
            if (time1 && time2) {
                let t1 = parseInt(time1.split(":")[0] * 60) + parseInt(time1.split(":")[1]);
                let t2 = parseInt(time2.split(":")[0] * 60) + parseInt(time2.split(":")[1]);
                return t1 <= t2;
            }
            return false;
        } catch (e) {
            return false;
        }
    },
    /*
     * @author : Kent
     * @desc: 比较两个时间的大小，支持的格式可在formatArr扩展
     * @param: datetime1:目标时间;datetime2:被比较时间
     */
    dateTimeCompare: function (datetime1, datetime2) {

        let formatArr = ['YYYY-MM-DD',
            'YYYY-MM-DD HH:mm',
            'YYYY-MM-DD HH:mm:ss'];//支持的格式
        try {
            if (datetime1 && datetime2) {
                let dt1 = moment(datetime1, formatArr);
                let dt2 = moment(datetime2, formatArr);
                //            console.info(dt1+","+dt2);
                return dt1 <= dt2;
            }
            return false;
        } catch (e) {
            return false;
        }
    },
}

//EasyUI用DataGrid格式化
let FormatterUtil = {
    /**布尔型格式化为：是/否
     * @return {string}
     */
    BoolFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }
        /*json格式时间转js时间格式*/
        if (value) {
            return "是";
        } else {
            return "否";
        }
    },
    /**性别格式化为：男/女
     * @return {string}
     */
    SexFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }
        /*json格式时间转js时间格式*/
        if (value) {
            return "男";
        } else {
            return "女";
        }
    },
    /**json格式日期格式化为：yyyy-MM-dd
     * @return {string}
     */

    DateFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }

        let dateValue;
        if (!$.string.isDate(value)) {
            /*json格式时间转js时间格式*/
            value = value.substr(1, value.length - 2);
            let obj = eval('(' + "{Date: new " + value + "}" + ')');
            dateValue = obj["Date"];
        } else {
            dateValue = $.string.toDate(value);
        }

        if (dateValue.getFullYear() < 1900) {
            return "";
        }

        return dateValue.format("yyyy-MM-dd");
    },
    /**json格式时间格式化为：HH:mm
     * @return {string}
     */
    TimeFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }

        let dateValue;
        if (!$.string.isDate(value)) {
            /*json格式时间转js时间格式*/
            value = value.substr(1, value.length - 2);
            let obj = eval('(' + "{Date: new " + value + "}" + ')');
            dateValue = obj["Date"];
        } else {
            dateValue = $.string.toDate(value);
        }

        if (dateValue.getFullYear() < 1900) {
            return "";
        }
        let val = dateValue.format("yyyy-MM-dd HH:mm");
        return val.substr(11, 5);
    },
    /**json格式时间日期格式化为：yyyy-MM-dd HH:mm
     * @return {string}
     */
    DateTimeFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }

        let dateValue;
        if (!$.string.isDate(value)) {
            /*json格式时间转js时间格式*/
            value = value.substr(1, value.length - 2);
            let obj = eval('(' + "{Date: new " + value + "}" + ')');
            dateValue = obj["Date"];
        } else {
            dateValue = $.string.toDate(value);
        }

        if (dateValue.getFullYear() < 1900) {
            return "";
        }

        return dateValue.format("yyyy-MM-dd HH:mm");
    },
    /**json格式时间日期格式化为本地时间（东八区）：yyyy-MM-dd HH:mm
     * @return {string}
     */
    LocalDateTimeFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }

        let dateValue;
        if (!$.string.isDate(value)) {
            /*json格式时间转js时间格式*/
            value = value.substr(1, value.length - 2);
            let obj = eval('(' + "{Date: new " + value + "}" + ')');
            dateValue = obj["Date"];
        } else {
            dateValue = $.string.toDate(value);
        }

        if (dateValue.getFullYear() < 1900) {
            return "";
        }

        return dateValue.format("yyyy-MM-dd HH:mm");
    },
    /**json格式时间日期格式化为：yyyy-MM-dd HH:mm:ss
     * @return {string}
     */
    DateTimeSecondFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }

        let dateValue;
        if (!$.string.isDate(value)) {
            /*json格式时间转js时间格式*/
            value = value.substr(1, value.length - 2);
            let obj = eval('(' + "{Date: new " + value + "}" + ')');
            dateValue = obj["Date"];
        } else {
            dateValue = $.string.toDate(value);
        }

        if (dateValue.getFullYear() < 1900) {
            return "";
        }

        return dateValue.format("yyyy-MM-dd HH:mm:ss");
    },
    /**json格式时间日期格式化为本地时间（东八区）：yyyy-MM-dd HH:mm:ss
     * @return {string}
     */
    LocalDateTimeSecondFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') {
            return "";
        }

        let dateValue;
        if (!$.string.isDate(value)) {
            /*json格式时间转js时间格式*/
            value = value.substr(1, value.length - 2);
            let obj = eval('(' + "{Date: new " + value + "}" + ')');
            dateValue = obj["Date"];
        } else {
            dateValue = $.string.toDate(value);
        }

        if (dateValue.getFullYear() < 1900) {
            return "";
        }

        return dateValue.format("yyyy-MM-dd HH:mm:ss");
    },

    /**标题格式化为：8位字符串长度的缩写格式，xxxxxxxx...
     * @return {string}
     */
    TitleFormatter: function (value, rec, index) {
        if (value.length > 10) value = value.substr(0, 8) + "...";
        return value;
    },
    /**长标题格式化为：12位字符串长度的缩写格式，xxxxxxxxxxxx...
     * @return {string}
     */
    LongTitleFormatter: function (value, rec, index) {
        if (value.length > 15) value = value.substr(0, 12) + "...";
        return value;
    },

    /**Blob对象格式化为：下载链接
     * @return {string}
     */
    DownloadBlobFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') return '';
        let isArray = $.array.isArray(value);
        if (isArray) {
            let aList = [];
            for (var i = 0; i < value.length; i++) {
                let blob = value[i] ? value[i] : null;
                if (blob != null)
                    aList.push('<a style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis; width:60px;" title="' + blob.blobName + '" target="_blank" href="' + blob.downloadFileUrl + '" download="' + blob.blobName + '">' + blob.blobName + '</a>');
            }
            return aList.join('<br/>');
        }
        if (value.blobName == undefined) {
            let data = JSON.parse(value);
            return '<a style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis; width:60px;" title="' + data.blobName + '" target="_blank" href="' + data.downloadFileUrl + '" download="' + data.blobName + '">' + data.blobName + '</a>';
        }
        return '<a style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis; width:60px;" title="' + value.blobName + '" target="_blank" href="' + value.downloadFileUrl + '" download="' + value.blobName + '">' + value.blobName + '</a>';
    },
    /**Blob对象格式化为：显示图片
     * @return {string}
     */
    ShowImageBlobFormatter: function (value, rec, index) {
        if (value === undefined || value === null || value === '') return '';
        let isArray = $.array.isArray(value);
        if (isArray) {
            let aList = [];
            for (var i = 0; i < value.length; i++) {
                let blob = value[i] ? value[i] : null;
                if (blob != null)
                    aList.push('<img src="' + blob.showImageUrl + '" alt="' + blob.blobName + '" width="42" height="42" />');
            }

            return aList.join('&nbsp;&nbsp;');
        }
        return '<img src="' + value.showImageUrl + '" alt="' + value.blobName + '" width="42" height="42" />';
    },
};

/*
 * 对easyui-validatebox的验证类型的扩展
 */
$.extend($.fn.validatebox.defaults.rules, {
    //select空值验证
    selectNotNull: {
        validator: function (value, param) {
            //console.info(value);
            return $(param[0]).find("option:contains('" + value + "')").val() !== '';
            //return value!='';
        },
        message: "请选择"
    },
    //正整数
    pnum: {
        validator: function (value, param) {
            return /^[0-9]*[1-9][0-9]*$/.test(value);
        },
        message: "请输入正整数"
    },
    //非0开头正整数
    pznum: {
        validator: function (value, param) {
            return /^[1-9]*[1-9][0-9]*$/.test(value);
        },
        message: "请输入非0开头的正整数"
    },
    //正实数，包含小数
    num: {
        validator: function (value, param) {
            return /^\d+(\.\d+)?$/.test(value);
        },
        message: "请输入正整数或者小数"
    },
    //2位正整数，或精确两位小数
    numTwoOrPointTwo: {
        validator: function (value, param) {
            return /^([1-9]\d?(\.\d{1,2})?|0\.\d{1,2}|0)$/.test(value);
        },
        message: "请输入1到2位的正整数或者精确到2位的小数"
    },
    //6位正整数，或精确两位小数
    numSixOrPointTwo: {
        validator: function (value, param) {
            return /^(([0-9]|([1-9][0-9]{0,5}))((\.[0-9]{1,2})?))$/.test(value);
        },
        message: "请输入1到6位的正整数或者精确到2位的小数"
    },
    //过滤特殊字符
    filterSpecial: {
        validator: function (value, param) {

            //过滤空格
            let flag = /\s/.test(value);
            //过滤特殊字符串
            let pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】’‘《》；：”“'。，、？]");
            let specialFlag = pattern.test(value);
            return !flag && !specialFlag;
        },
        message: "非法字符，请重新输入"
    },

    //身份证
    IDCard: {
        validator: function (value, param) {
            if (value === undefined || value == null || value === "")
                return false;
            //需要引用：jquery.extension.core.js（针对String对象的扩展）
            let flag = String.isIDCard(value);
            return flag === true;
        },
        message: "请输入正确的身份证号码"
    },
    //统一社会信用编码
    SocialCreditCode: {
        validator: function (value, param) {
            if (value === undefined || value == null || value === "")
                return false;
            //需要引用：jquery.extension.core.js（针对String对象的扩展）
            let flag = String.isSocialCreditCode(value);
            return flag === true;
        },
        message: "请输入正确的统一社会信用编码"
    },
    //营业执照代码
    BusinessCode: {
        validator: function (value, param) {
            if (value === undefined || value == null || value === "")
                return false;
            //需要引用：jquery.extension.core.js（针对String对象的扩展）
            let flag = String.isBusinessCode(value);
            return flag === true;
        },
        message: "请输入正确的营业执照代码"
    },
    //组织机构代码
    OrganizationCode: {
        validator: function (value, param) {
            if (value === undefined || value == null || value === "")
                return false;
            //需要引用：jquery.extension.core.js（针对String对象的扩展）
            let flag = String.isOrganizationCode(value);
            return flag === true;
        },
        message: "请输入正确的组织机构代码"
    },
    //纳税人识别号
    TaxpayerCode: {
        validator: function (value, param) {
            if (value === undefined || value == null || value === "")
                return false;
            //需要引用：jquery.extension.core.js（针对String对象的扩展）
            let flag = String.isTaxpayerCode(value);
            return flag === true;
        },
        message: "请输入正确的纳税人识别号"
    },

    mdStartDate: {
        validator: function (value, param) {
            let startTime2 = $(param[0]).datetimebox('getValue');
            let d1 = $.fn.datebox.defaults.parser(startTime2);
            let d2 = $.fn.datebox.defaults.parser(value);
            varify = d2 <= d1;
            return varify;
        },
        message: '开始时间必须小于或等于结束时间！'
    },
    //比较日期选择器
    compareDate: {
        validator: function (value, param) {
            let startTime = $(param[0]).val();
            if (startTime === undefined || startTime === null || startTime === '')
                startTime = $(param[0]).date("getValue");
            if (startTime === undefined || startTime === null || startTime === '')
                startTime = $(param[0]).datetimebox("getValue");
            return CompareUtil.dateCompare(startTime, value);
        },
        message: "结束日期不能小于或等于开始日期"
    },
    //比较时间选择器（时分秒）
    compareTime: {
        validator: function (value, param) {
            let startTime = $(param[0]).val();
            if (startTime === undefined || startTime === null || startTime === '')
                startTime = $(param[0]).timespinner("getValue");
            return CompareUtil.timeCompare(startTime, value);
        },
        message: "结束时间不能小于或等于开始时间"
    },
    //比较日期时间选择器（时分秒）
    compareDateTime: {
        validator: function (value, param) {
            let startTime = $(param[0]).val();
            if (startTime === undefined || startTime === null || startTime === '')
                startTime = $(param[0]).timespinner("getValue");
            return CompareUtil.dateTimeCompare(startTime, value);
        },
        message: "结束时间不能小于或等于开始时间"
    },
    //验证是否包含空格和非法字符
    unnormal: {
        validator: function (value) {
            return /^[a-zA-Z0-9]/i.test(value);

        },
        message: '输入值不能为空和包含其他非法字符'
    },
    //验证名称是否存在
    existName: {
        validator: function (value, param) {
            let flag = true;
            let url = param[0];
            let id = param[1];
            let isEditMode = param[2];
            let orginalName = param[3];
            $.ajax({
                async: false,
                type: 'POST',
                dataType: 'json',
                url: url,
                data: {
                    id: id,
                    name: value,
                    isEditMode: isEditMode,
                    orginalName: orginalName,
                },
                success: function (result) {
                    if (result.toString() == "true") {
                        flag = false;
                    }
                }
            });
            return flag;
        },
        message: '{4}'
    },
    remote: {
        validator: function (value, param) {
            let flag = true;
            let postData = {};
            postData[param[1]] = value;
            $.ajax({
                async: false,
                type: 'POST',
                dataType: 'json',
                timeout: 2000,
                url: param[0],
                data: postData,
                success: function (result) {
                    debugger;
                    if (result.toString() === "true") {
                        flag = false;
                    }
                }
            });
            return flag;
        },
        message: "{2}"
    },
    dynamicRemote: {
        validator: function (value, param) {
            let flag = true;
            let postData = {};
            postData[param[1]] = value;
            $.ajax({
                async: false,
                type: 'POST',
                dataType: 'json',
                timeout: 2000,
                url: param[0],
                data: postData,
                success: function (result) {
                    if (!result.success) {
                        flag = false;
                        $.fn.validatebox.defaults.rules.dynamicRemote.message = result.message;
                    }
                }
            });
            return flag;
        },
        message: ""
    }
});

/**
 * 获取选中的option对象
 */
$.extend($.fn.combobox.methods, {
    getSelectRow: function (jq) {
        let state = $.data(jq[0], 'combobox');
        let opts = state.options;
        let data = state.data;
        let selected = $(jq[0]).combobox('getValue');
        for (let i = 0; i < data.length; i++) {
            let dataValue = data[i][opts.valueField].toString();
            if (dataValue === selected) {
                return data[i];
            }
        }
    }
});

/*
 * 对easyui-treegrid的树表格类型的扩展
 */
$.extend($.fn.treegrid.methods, {
    //isContains是否包含父节点（即子节点被选中时是否也取父节点）
    getAllChecked: function (jq, isContains) {
        let keyValues = [];
        /*
          tree-checkbox2 有子节点被选中的css
          tree-checkbox1 节点被选中的css
          tree-checkbox0 节点未选中的css
        */
        let checkNodes = jq.treegrid("getPanel").find(".tree-checkbox1");
        for (let i = 0; i < checkNodes.length; i++) {
            let keyValue1 = $($(checkNodes[i]).closest('tr')[0]).attr("node-id");
            keyValues.push(keyValue1);
        }

        if (isContains) {
            let childCheckNodes = jq.treegrid("getPanel").find(".tree-checkbox2");
            for (let i = 0; i < childCheckNodes.length; i++) {
                let keyValue2 = $($(childCheckNodes[i]).closest('tr')[0]).attr("node-id");
                keyValues.push(keyValue2);
            }
        }

        return keyValues;
    }
});

/*
 * 对easyui-datagrid的表格类型的扩展
 */
$.extend($.fn.datagrid.methods, {
    /*
    *  datagrid 获取行的行索引：
    *  $('#id').datagrid('getRowIndex'); //获取当前datagrid中在编辑状态的行编号列表
    */
    getRowIndex: function (jq) {
        let tr = $(jq).closest('tr.datagrid-row');
        return parseInt(tr.attr('datagrid-row-index'));
    },
    /*
    *  datagrid 获取正在编辑状态的行索引列表，使用如下：
    *  $('#id').datagrid('getEditingRowIndexs'); //获取当前datagrid中在编辑状态的行编号列表
    */
    getEditingRowIndexs: function (jq) {
        let rows = $.data(jq[0], "datagrid").panel.find('.datagrid-row-editing');
        let indexes = [];
        rows.each(function (i, row) {
            let index = row.sectionRowIndex;
            if (indexes.indexOf(index) === -1) {
                indexes.push(index);
            }
        });
        return indexes;
    },
    /*
    *  datagrid 根据列的Filed的值，获取当前列的名称，使用如下：
    *  $('#id').datagrid('getColumnTitleByField', 'id'); //获取当前datagrid中在编辑状态的行编号列表
    */
    getColumnTitleByField: function (jq, field) {
        let columns = $(jq).datagrid('getColumnFields');
        let result = '';
        for (let i in columns) {
            //获取每一列的列名对象
            if (columns.hasOwnProperty(i)
                && typeof columns[i] != "function") {
                let col = $(jq).datagrid("getColumnOption", columns[i]);
                if (col.field === field)
                    result = col.title;
            }
        }

        return result;
    },
    /*
    *  datagrid 根据所有列的属性值列表，使用如下：
    *  $('#id').datagrid('getAllColumnProperties');
    * 结果：[{filed:'',title:''},{filed:'',title:''},...]
    */
    getAllColumnProperties: function () {
        let columns = $(dgProductId).datagrid('getColumnFields');
        let properties = [];
        for (let i in columns) {
            let property = {};
            //获取每一列的列名对象
            if (columns.hasOwnProperty(i)
                && typeof columns[i] != "function"
                && columns[i].startsWith(dynamicColumnId)) {
                let col = $(dgProductId).datagrid("getColumnOption", columns[i]);
                property.field = col.field;
                property.title = col.title;
                //追加对象
                properties.push(property);
            }
        }

        return properties;
    },
    /*
    *  datagrid 下添加编辑器：
    *  $('#id').datagrid('addEditor', column); //获取当前datagrid中在编辑状态的行编号列表
    */
    addEditor: function (jq, param) {
        if (param instanceof Array) {
            $.each(param, function (index, item) {
                let e = $(jq).datagrid('getColumnOption', item.field);
                e.editor = item.editor;
            });
        } else {
            let e = $(jq).datagrid('getColumnOption', param.field);
            e.editor = param.editor;
        }
    },
    /*
    *  datagrid 下删除编辑器：
    *  $('#id').datagrid('removeEditor', column); //获取当前datagrid中在编辑状态的行编号列表
    */
    removeEditor: function (jq, param) {
        if (param instanceof Array) {
            $.each(param, function (index, item) {
                let e = $(jq).datagrid('getColumnOption', item);
                e.editor = {};
            });
        } else {
            let e = $(jq).datagrid('getColumnOption', param);
            e.editor = {};
        }
    }
});

/*
 * 对easyui-datagrid的editor的扩展
 */
$.extend($.fn.datagrid.defaults.editors, {
    datebox: {
        index: 0,
        init: function (container, options) {
            let name = 'datebox_' + this.index++;
            let input = $('<input name="' + name + '" type="text">').appendTo(container);
            input.datebox(options);
            return input;
        },
        destroy: function (target) {
            $(target).datebox('destroy');
        },
        getValue: function (target) {
            let oldValue = $(target).datebox('getValue');//获得旧值
            return oldValue;
        },
        setValue: function (target, value) {
            if ("99999999" === value) {
                $(target).datebox('setValue', "2099-12-31");//设置新值的日期格式
            } else {
                $(target).datebox('setValue', value);//设置新值的日期格式
            }

        },
        resize: function (target, width) {
            $(target).datebox('resize', width);
        }
    },
    /*
    * RadionButton编辑器，使用示例如下：
        editor:{
            type:'radioGroup',
            options:{
                value: '0',
                labelPosition: 'after',
                labelWidth: 40,
                checked: true,
                items:[
                    {value:'0',text:'面议'},
                    {value:'1',text:'单价'}
                ]
            }
        }
    * */
    radioGroup: {
        index: 0,
        init: function (container, options) {
            let span = $('<span></span>').appendTo(container);
            let name = 'radio_' + this.index++;
            $.map(options.items || [], function (item) {
                let checked = item.value === options.value ? 'checked' : '';
                let input = null;
                if (checked)
                    input = $('<input data-options="checked:true," class="easyui-radiobutton" name="' + name + '" value="' + item.value + '" label="' + item.text + '"/>').appendTo(span);
                else
                    input = $('<input class="easyui-radiobutton" name="' + name + '" value="' + item.value + '" label="' + item.text + '"/>').appendTo(span);
                input.radiobutton(options);
            });
            return span;
        },
        destroy: function (target) {
            $(target).remove();
        },
        getValue: function (target) {
            return $(target).find('input:checked').val();
        },
        setValue: function (target, value) {
            $(target).find('input[value=' + value + ']')._propAttr('checked', true);
        },
        resize: function (target, width) {

        }
    },
    /*
    * ImageUploader编辑器，使用示例如下：
        field: 'productImageBlobs',
        title: '图片【格式：[[${uploadConfig.imageExt}]]，大小：[[${uploadConfig.imageMaxSize}]]M】',
        width: 360,
        align: 'left',
        formatter: function (value, row, index) {
            //debugger;
            let tdContext = '';
            if (value !== undefined && value != null && value.length > 0) {
                tdContext = '<div class="webUploader-image-list showImage">';
                $.each(value, function (i, blob) {
                    blob.imageUrl = '/Home/ShowTempImage?id=' + blob.id;
                    tdContext += imageUploader.getImageListHtmlView(blob, false, false);
                });
                tdContext += "</div>";
            }
                            return tdContext;
       },
       editor: {
            type: 'imageUploader',
            options: {
                //设置预览图片的标签：<p id="imagePreviewContainer" class="imagePreview"></p>
                imagePreviewContainer: $('#imagePreviewContainer'),
                fileNumLimit: imageNumLimit,
                configure: {
                    imageMaxSize: imageSize,
                    imageExt: imageExt,
                    fileMaxSize: fileMaxSize,
                    fileExt: fileExt
                },
                callback: {
                    onRemoveImage: function (file) {
                        //新上传未保存至数据库的图片，未生成propertyId
                        if (file.propertyId !== undefined && file.propertyId != null && file.propertyId !== '')
                            removeProductPropertyIds.push(file.propertyId);

                        return true;
                    }
               }
            }
        }
    * */
    imageUploader: {
        index: 0,
        dgImageUploader: {},
        init: function (container, options) {
            //debugger;
            let idx = '-' + this.index++;
            let uploaderId = "imageUploader" + idx;
            let divUploader = '<div class="imageUploaderEditor" id="' + uploaderId + '">' +
                '<div>' +
                '   <div style="display: flex;height: 130px;">' +
                '       <div id="btnAddImageFile' + idx + '">' +
                '           <a href="javascript:void(0)"><i class="fa fa-plus-square fa-5x"></i></a>' +
                '       </div>' +
                '       <div id="imageFileList' + idx + '" style="width:280px;" class="webUploader-image-list"></div>' +
                '   </div>' +
                '</div>' +
                '</div>';

            let input = $(divUploader).appendTo(container);

            let settings = {
                componentName: uploaderId,
                btnAddFile: '#btnAddImageFile' + idx,
                fileList: '#imageFileList' + idx,
                //params: {ids: options.ids},
                isRegister: true,
                isSetDefault: true,
                isEditor: true,
                callback: {
                    uploadStart: function (file) {
                    },
                    uploadProgress: function (file, percentage) {
                    },
                    uploadComplete: function (file) { //不管成功或者失败，文件上传完成时触发
                    },
                    uploadSuccess: function (file, response, blob) {
                    },
                    uploadError: function (file, reason) {
                        let fileName = file.name;
                        console.error("---file[" + fileName + "] throw error: " + reason);
                    },
                    onFileQueued: function (file) {
                    }
                }
            };
            $.extend(true, settings, options);
            this.dgImageUploader[uploaderId] = imageUploader(settings);

            return input;
        },
        destroy: function (target) {
            $(target).remove();
        },
        getValue: function (target) {
            //debugger;
            let blobs = [];
            let txtBlobs = $(target).find('input.imageBlobId');
            if (txtBlobs !== undefined && txtBlobs != null && txtBlobs.length > 0) {
                $.each(txtBlobs, function (i, txtBlob) {
                    let isSelect = false;
                    let blobId = $(txtBlob).val();
                    let blobName = $(txtBlob).attr('title');
                    let ext = $(txtBlob).attr('ext');
                    let size = $(txtBlob).attr('size');
                    let propertyId = $(txtBlob).attr('propertyId');
                    let checked = $(txtBlob).attr('checked');
                    if (checked !== undefined
                        && checked != null
                        && checked !== '') {
                        isSelect = true;
                    }
                    let blob = {};
                    blob.blobId = blobId;
                    blob.blobName = blobName;
                    blob.ext = ext;
                    blob.size = size;
                    blob.propertyId = propertyId;
                    blob.isSelect = isSelect;
                    blobs.push(blob);
                });
            }
            return blobs;
        },
        setValue: function (target, value) {
            if (value !== undefined && value != null && value.length > 0) {
                //debugger;
                let uploaderId = target[0].id;
                let uploader = this.dgImageUploader[uploaderId];
                uploader.initImageView(value);
            }
        },
        resize: function (target, width) {
            //$(target).numberspinner('resize',width);
        }
    },
    /*
     * FileUploader文件上传编辑器，使用示例如下：
    //上传的前端div容器
    //fileNumLimit设置为：1，btnAddFile设置为：上传按钮的div容器id筛选器，fileList设置为：文件展示的div容器id筛选器
    field: 'productFileBlobs',
    title: '技术文档【格式：[[${uploadConfig.fileExt}]]，大小：[[${uploadConfig.fileMaxSize}]]M】',
    width: 360,
    align: 'left',
    formatter: function (value, row, index) {
        //debugger;
        let tdContext = '';
        if (value !== undefined && value != null) {
            //单文件
            //value.imageUrl = '/Home/ShowTempImage?id=' + value.blobId;
            //tdContext += fileUploader.getFileListHtmlView(value, false, true);
            //多文件
            tdContext = '<div class="webUploader-file-list">';
            $.each(value, function (i, blob) {
                blob.imageUrl = '/Home/ShowTempImage?id=' + blob.id;
                tdContext += fileUploader.getFileListHtmlView(blob, false, false);
            });
            tdContext += "</div>";
        }
                        return tdContext;
    },
    editor: {
        type: 'fileUploader',
        options: {
            fileNumLimit: 3,
            configure: {
                imageMaxSize: imageSize,
                imageExt: imageExt,
                fileMaxSize: fileMaxSize,
                fileExt: fileExt
            },
            callback: {
                onRemoveImage: function (file) {
                    //新上传未保存至数据库的图片，未生成propertyId
                    if (file.propertyId !== undefined && file.propertyId != null && file.propertyId !== '')
                        removeProductPropertyIds.push(file.propertyId);

                    return true;
                }
            }
        }
    }
    * */
    fileUploader: {
        index: 0,
        isSingleFile: false,
        dgFileUploader: {},
        init: function (container, options) {
            //debugger;
            let idx = '-' + this.index++;
            let uploaderId = "fileUploader" + idx;
            this.isSingleFile = options.fileNumLimit !== undefined && options.fileNumLimit != null
                ? options.fileNumLimit === 1
                : false;
            let divUploader = '<div class="fileUploaderEditor" id="' + uploaderId + '">' +
                '<div>' +
                '   <div style="display: flex;height: 130px;">' +
                '       <div id="btnAddDocFile' + idx + '">' +
                '           <a href="javascript:void(0)"><i class="fa fa-plus-square fa-5x"></i></a>' +
                '       </div>' +
                '       <div id="docFileList' + idx + '" style="width:280px;" class="webUploader-file-list"></div>' +
                '   </div>' +
                '</div>' +
                '</div>';
            if (this.isSingleFile) {
                divUploader = '<div class="fileUploaderEditor" style="display: flex;" id="' + uploaderId + '">' +
                    '<div id="docFileList' + idx + '" class="webUploader-file-single"></div>' +
                    '<a id="btnAddDocFile' + idx + '" href="javascript:void(0)" class="easyui-linkbutton btnSelect" iconcls="fa fa-pencil">选择</a>' +
                    '</div>';
            }

            let input = $(divUploader).appendTo(container);

            $(".btnSelect").linkbutton({ iconCls: 'fa fa-pencil' });    //日志
            let settings = {
                componentName: uploaderId,
                btnAddFile: '#btnAddDocFile' + idx,
                fileList: '#docFileList' + idx,
                //params: {ids: options.ids},
                isRegister: true,
                isEditor: true,
                callback: {
                    uploadStart: function (file) {
                    },
                    uploadProgress: function (file, percentage) {
                    },
                    uploadComplete: function (file) { //不管成功或者失败，文件上传完成时触发
                    },
                    uploadSuccess: function (file, response, blob) {
                    },
                    uploadError: function (file, reason) {
                        let fileName = file.name;
                        console.error("---file[" + fileName + "] throw error: " + reason);
                    },
                    onFileQueued: function (file) {
                    }
                }
            };
            $.extend(true, settings, options);
            this.dgFileUploader[uploaderId] = fileUploader(settings);

            return input;
        },
        destroy: function (target) {
            $(target).remove();
        },
        getValue: function (target) {
            //debugger;
            let blobs = [];
            let txtBlobs = $(target).find('input.fileBlobId');
            if (txtBlobs !== undefined && txtBlobs != null && txtBlobs.length > 0) {
                $.each(txtBlobs, function (i, txtBlob) {
                    let isSelect = false;
                    let blobId = $(txtBlob).val();
                    let blobName = $(txtBlob).attr('title');
                    let ext = $(txtBlob).attr('ext');
                    let size = $(txtBlob).attr('size');
                    let propertyId = $(txtBlob).attr('propertyId');
                    let checked = $(txtBlob).attr('checked');
                    if (checked !== undefined
                        && checked != null
                        && checked !== '') {
                        isSelect = true;
                    }
                    let blob = {};
                    blob.blobId = blobId;
                    blob.blobName = blobName;
                    blob.ext = ext;
                    blob.size = size;
                    blob.propertyId = propertyId;
                    blob.isSelect = isSelect;
                    blobs.push(blob);
                });
            }

            if (this.isSingleFile)
                return blobs.length === 0 ? null : blobs[0];

            return blobs;
        },
        setValue: function (target, value) {
            if (value !== undefined && value != null) {
                //debugger;
                let uploaderId = target[0].id;
                let uploader = this.dgFileUploader[uploaderId];
                if (uploader !== undefined && uploader != null)
                    uploader.initFileView(value);
            }
        },
        resize: function (target, width) {
            //$(target).numberspinner('resize',width);
        }
    },
});
