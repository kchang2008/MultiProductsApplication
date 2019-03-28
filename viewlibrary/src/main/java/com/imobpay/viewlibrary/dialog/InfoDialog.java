package com.imobpay.viewlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imobpay.viewlibrary.R;
import com.imobpay.viewlibrary.interfaces.InfoDialogListener;


/**
 * 信息提示框 com.qtpay.imobpay.dialog.InfoDialog
 * 
 * @author 王海军
 * @说明：信息提示，并根据定义的文字显示;根据外部要求对功能响应给出反馈 ---------------------
 * | 标题 | 关闭 |
 * |--------------------|
 * | 信息提示内容区 |
 * ----------------------
 * | 左功能 | 右功能 |
 * ----------------------
 * @接口：无 @说明：无
 * @接口：无 @说明：无
 * @接口：无 @说明：无
 * Create at 2016年3月14日 下午2:05:42
 */
public class InfoDialog extends BaseDialog {

    TextView dialog_info_left_button; // 左侧功能按钮
    TextView dialog_info_right_button; // 右侧功能按钮
    TextView dialog_info_tips; // 信息提示文字
    TextView dialog_info_title; // 标题说明
    TextView dialog_info_title_right; // 标题右侧的文字功能,可以是关闭功能或者其他
    LinearLayout diaolog_info_img_lin;
    LinearLayout lin_line;

    public void setInfoDialogListener(InfoDialogListener infoDialogListener) {
        this.infoDialogListener = infoDialogListener;
    }

    InfoDialogListener infoDialogListener; // 监听器

    private final int TITLE_RIGHT_CLICKED = 0; // 标题右侧文字区域按下
    private final int LEFT_BUTTON_CLICKED = 1; // 左侧按钮区域按下
    private final int RIGHT_BUTTON_CLICKED = 2; // 右侧按钮区域按下

    public InfoDialog(Context context, int theme) {
        super(context, theme);
    }
    public InfoDialog(Context context, int theme, InfoDialogListener listener) {
        super(context, theme);
        infoDialogListener = listener;
    }

    /**
     * doClick
     *
     * @return 无
     * @throws
     * @说明: 响应不同区域的按键处理，具体处理由外部处理
     * @Parameters 无
     */
    public void doClick(int flag) {
        if (infoDialogListener == null) {
            return;
        }

        switch (flag) {
            case TITLE_RIGHT_CLICKED:
                infoDialogListener.onTitleRightClicked();
                break;
            case LEFT_BUTTON_CLICKED:
                infoDialogListener.onLeftButtonClicked();
                break;
            case RIGHT_BUTTON_CLICKED:
                infoDialogListener.onRightButtonClicked();
                break;
        }
    }

    /**
     * 按照格式赋初值
     *
     * @return 无
     * @throws
     * @说明：title_right_visible控制标题右边关闭是否显示 left_button_visible控制左功能按钮是否显示
     * right_button_visibile控制右功能按钮是否显示
     * @Parameters title, title_right, tips, left_button, right_button
     */
    public void setParams(String title_text,
                          String title_right_text,
                          String tips_text,
                          String left_button_text,
                          String right_button_text,
                          boolean title_right_visible,
                          boolean left_button_visible,
                          boolean right_button_visibile
    ) {

        dialog_info_title.setText(getDefaultValue(title_text));
        dialog_info_title_right.setText(getDefaultValue(title_right_text));
        dialog_info_tips.setText(getDefaultValue(tips_text));
        dialog_info_right_button.setText(getDefaultValue(right_button_text));
        dialog_info_left_button.setText(getDefaultValue(left_button_text));

        if (!isNotEmptyOrNull(title_text)) {
            dialog_info_title.setVisibility(View.GONE);
        }

        if (!title_right_visible) {
            dialog_info_title_right.setVisibility(View.GONE);
        }
        if (!right_button_visibile) {
            dialog_info_right_button.setVisibility(View.GONE);
            diaolog_info_img_lin.setVisibility(View.GONE);
        }
        if (!left_button_visible) {
            dialog_info_left_button.setVisibility(View.GONE);
            diaolog_info_img_lin.setVisibility(View.GONE);
        }
        if (!right_button_visibile && !left_button_visible) {
            lin_line.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_info);

        setCanceledOnTouchOutside(false);

        dialog_info_tips = (TextView) findViewById(R.id.dialog_info_tips);
        dialog_info_title = (TextView) findViewById(R.id.dialog_info_title);
        dialog_info_title_right = (TextView) findViewById(R.id.dialog_info_title_right);
        dialog_info_right_button = (TextView) findViewById(R.id.dialog_info_right_button);
        dialog_info_left_button = (TextView) findViewById(R.id.dialog_info_left_button);
        lin_line = (LinearLayout) findViewById(R.id.lin_linev);
        diaolog_info_img_lin = (LinearLayout) findViewById(R.id.dialog_info_img_lin);
        dialog_info_title_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doClick(TITLE_RIGHT_CLICKED);
            }
        });

        dialog_info_right_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doClick(RIGHT_BUTTON_CLICKED);
            }
        });

        dialog_info_left_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doClick(LEFT_BUTTON_CLICKED);
            }
        });
    }

}
