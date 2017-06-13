package com.codit.interview.aptitude;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by Sreejith on 10-Mar-16.
 */
public class CalcFragment extends DialogFragment implements ImageButton.OnClickListener{

    CalcInterface interfaceObj;
    SharedPreferences sharedPref;

   public interface CalcInterface
    {
         void calcCopy(String history);
    }

    boolean showNotes;
    Toolbar calcTitleToolbar;
    TextView history;
    LinearLayout historyLayout;
    LinearLayout calcParent;
    CoordinatorLayout coordinatorLayout;

    Button no1;
    Button no2;
    Button no3;
    Button no4;
    Button no5;
    Button no6;
    Button no7;
    Button no8;
    Button no9;
    Button no0;
    Button opPlus,opMinus,opDiv,opEquals,opMultiply,numDot;
    Button opNegate;
    ImageButton opBackSpace;
    Button opClear;
    TextView input,output;

    String num1,num2,currentOp,previousOp;
    boolean opAgain;
    boolean resetInput;


    @Override
    public void onResume() {
        super.onResume();

        int width=getResources().getDisplayMetrics().widthPixels;
        int widthPixels=width-width/10;



        getDialog().getWindow().setLayout(widthPixels,ViewGroup.LayoutParams.WRAP_CONTENT);
        showNotes=false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            interfaceObj=(CalcInterface) activity;
        }

        catch (ClassCastException e)
        {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),"fonts/ufonts.com_digital-computer-calculator.ttf");
        Typeface tfi = Typeface.createFromAsset(getResources().getAssets(),"fonts/digital-7 (italic).ttf");


        opAgain=false;
        resetInput=false;

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setCancelable(false);


        LayoutInflater inflater=getActivity().getLayoutInflater();


        View view=inflater.inflate(R.layout.layout_calc, null);

        coordinatorLayout=(CoordinatorLayout)getActivity().findViewById(R.id.queActivity);



        int heightPixels=getResources().getDisplayMetrics().heightPixels;
        int height=heightPixels-heightPixels/3;


        //calc parent
        calcParent=(LinearLayout)view.findViewById(R.id.calcParent);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) calcParent.getLayoutParams();
        params.height=height;

        calcParent.setLayoutParams(params);
        //History text view
        history=(TextView)view.findViewById(R.id.textHistory);
        historyLayout=(LinearLayout)view.findViewById(R.id.historyLayout);

        //numbers
        no0=(Button)view.findViewById(R.id.num0);
        no1=(Button)view.findViewById(R.id.num1);
        no2=(Button)view.findViewById(R.id.num2);
        no3=(Button)view.findViewById(R.id.num3);
        no4=(Button)view.findViewById(R.id.num4);
        no5=(Button)view.findViewById(R.id.num5);
        no6=(Button)view.findViewById(R.id.num6);
        no7=(Button)view.findViewById(R.id.num7);
        no8=(Button)view.findViewById(R.id.num8);
        no9=(Button)view.findViewById(R.id.num9);
        numDot=(Button)view.findViewById(R.id.numDot);



        //number onclick listeners
        no0.setOnClickListener(this);

        no1.setOnClickListener(this);
        no2.setOnClickListener(this);
        no3.setOnClickListener(this);
        no4.setOnClickListener(this);
        no5.setOnClickListener(this);
        no6.setOnClickListener(this);
        no7.setOnClickListener(this);
        no8.setOnClickListener(this);
        no9.setOnClickListener(this);
        numDot.setOnClickListener(this);

        //clear
        opClear= (Button) view.findViewById(R.id.clear);
        opClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opAgain=false;
                resetInput=false;
                //dotFlag=false;

                num1=null;
                num2=null;
                previousOp=null;

                input.setText("0");
                output.setText("");

            }
        });


        //backspace
        opBackSpace=(ImageButton)view.findViewById(R.id.backspace);
        opBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(input.getText().toString().equals("INVALID"))
                {
                    return;
                }

                if(input.toString()!="0")
                {
                    StringBuffer buffer=new StringBuffer(String.valueOf(input.getText()));
                    if(buffer.length()==1)
                    {
                        input.setText("0");
                    }
                    else
                    {
                        buffer.deleteCharAt(buffer.length() - 1);
                        input.setText(String.valueOf(buffer));
                    }
                }
            }
        });

        opNegate=(Button) view.findViewById(R.id.negate);
        opNegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(resetInput)
                {
                    input.setText("-");
                    resetInput=false;
                    return;
                }
                //StringBuffer buffer=new StringBuffer(String.valueOf(input.getText()));
                if(input.getText().charAt(0)!='-')
                {


                    if(input.getText().toString().equals("0"))
                    {
                        input.setText("-");
                    }
                    else {
                        input.setText("-" + input.getText());
                    }

                }
                else
                {

                    if(input.getText().toString().equals("-"))
                    {
                        input.setText("0");
                        return;

                    }

                    StringBuffer buffer=new StringBuffer(String.valueOf(input.getText()));
                    buffer.deleteCharAt(0);
                    input.setText(buffer);
                }
            }
        });

        //operators
        opPlus=(Button) view.findViewById(R.id.opAdd);
        opMinus=(Button)view.findViewById(R.id.opSub);
        opMultiply=(Button)view.findViewById(R.id.opMultiply);
        opDiv=(Button)view.findViewById(R.id.opDiv);
        opEquals=(Button)view.findViewById(R.id.opEquals);



        //operator listeners
        opEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opListener(v);
            }
        });

        opMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opListener(v);
            }
        });

        opPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opListener(v);
            }
        });

        opDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opListener(v);
            }
        });

        opMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opListener(v);
            }
        });

        //Text views
        input=(TextView)view.findViewById(R.id.input);
        output=(TextView)view.findViewById(R.id.output);

        input.setTypeface(tf);
        output.setTypeface(tfi);




        builder.setView(view);
        View calcTitle=inflater.inflate(R.layout.calc_title,null);

        calcTitleToolbar=(Toolbar)calcTitle.findViewById(R.id.calcTitle);
        calcTitleToolbar.setTitleTextColor(Color.WHITE);
        
        calcTitleToolbar.setBackgroundColor(Color.BLACK);
        Drawable back;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            back=getResources().getDrawable(R.drawable.ic_back_arrow);
        }
        else
        {
            back=getResources().getDrawable(R.drawable.ic_back);
        }


        calcTitleToolbar.setNavigationIcon(back);
        calcTitleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });


        calcTitleToolbar.inflateMenu(R.menu.calc_title_menu);


        calcTitleToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.buttonHistory:


                        showOrCloseHistory(historyLayout, calcParent, item);

                        if(copyToNotes())
                        {
                            Toast.makeText(getContext(),"Calculations copied to notes !",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(getContext(),"History Empty !",Toast.LENGTH_SHORT).show();

                        }

                        break;



                }
                return true;
            }
        });

        builder.setCustomTitle(calcTitle);

         Dialog dialog=builder.create();

        dialog.getWindow().getAttributes().windowAnimations=R.style.CalcAnimation;


        return dialog;

    }



    public boolean copyToNotes()
    {
        if(!String.valueOf(history.getText()).equals("Do some calculations and they will appear here !"))
        {

            interfaceObj.calcCopy(String.valueOf(history.getText()));

            return true;


        }
        else
        {
            return false;
        }


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(sharedPref.getBoolean("calc_save",true)) {
            SharedPreferences progressPreference=getContext().getSharedPreferences("progress", Context.MODE_PRIVATE);

            if(copyToNotes()&&progressPreference.getInt("visitCount",0)==1)
                Toast.makeText(getContext(),"Calculations copied to notes !",Toast.LENGTH_SHORT).show();


        }

    }

    private void showOrCloseHistory(final LinearLayout historyLayout, final LinearLayout calcParent, final MenuItem item)
    {



        //show history
        if(historyLayout.getVisibility()==View.GONE&&calcParent.getVisibility()==View.VISIBLE)
        {
            //set back button to navigate to calculator
            calcTitleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //calcTitleToolbar.getMenu().findItem(R.id.buttonCopy).setVisible(false);

                    item.setIcon(R.drawable.ic_calc_copy);
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            showOrCloseHistory(historyLayout,calcParent,item);
                            if(copyToNotes())
                            {
                                Toast.makeText(getContext(),"Calculations copied to notes !",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getContext(),"History Empty !",Toast.LENGTH_SHORT).show();

                            }
                            return true;
                        }
                    });

                    calcTitleToolbar.setTitle("");
                    historyLayout.setVisibility(View.GONE);
                    calcParent.setVisibility(View.VISIBLE);

                    calcTitleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getDialog().dismiss();
                        }
                    });

                }
            });

            calcTitleToolbar.setTitle("History");

            item.setIcon(R.drawable.ic_history_close);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    history.setText("");
                    history.setGravity(Gravity.CENTER);
                    history.setText("Do some calculations and they will appear here !");
                    return true;
                }
            });


            //calcTitleToolbar.getMenu().findItem(R.id.buttonCopy).setVisible(true);

            calcParent.setVisibility(View.GONE);

            historyLayout.setVisibility(View.VISIBLE);

        }
        //close history
        else if(historyLayout.getVisibility()==View.VISIBLE&&calcParent.getVisibility()==View.GONE)
        {

            calcTitleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   getDialog().dismiss();

                }
            });

            //calcTitleToolbar.getMenu().findItem(R.id.buttonCopy).setVisible(false);

            calcTitleToolbar.setTitle("");
            //item.setIcon(R.drawable.ic_calc_history);
            historyLayout.setVisibility(View.GONE);
            calcParent.setVisibility(View.VISIBLE);

        }



    }

    public void opListener(View operator)

    {

        String value = input.getText().toString();

            switch (operator.getId())
            {

                case R.id.opAdd:
                     currentOp=" + ";
                        sentToOutput(value,currentOp,opAgain);
                    break;

                case R.id.opDiv:

                    currentOp=" ÷ ";
                        sentToOutput(value,currentOp,opAgain);
                    break;

                case R.id.opMultiply:
                  currentOp=" x ";
                        sentToOutput(value,currentOp,opAgain);
                    break;

                case R.id.opSub:

                    currentOp=" - ";
                        sentToOutput(value,currentOp,opAgain);
                    break;

                case R.id.opEquals:

                    currentOp="=";
                    sentToOutput(value, currentOp, opAgain);
            }

    }

    public String calculate(String num1,String num2,String op)
    {
        double result = 0;

        try {
            double n1 = Double.valueOf(num1);
            double n2 = Double.valueOf(num2);


            switch (op) {
                case " + ":
                    result = n1 + n2;
                    break;

                case " - ":

                    result = n1 - n2;
                    break;

                case " ÷ ":

                    result = n1 / n2;
                    break;

                case " x ":

                    result = n1 * n2;
                    break;

                default:

            }
            if (result % 1 == 0) {
                DecimalFormat decimalFormat = new DecimalFormat(".");
                decimalFormat.setGroupingUsed(false);
                decimalFormat.setDecimalSeparatorAlwaysShown(false);

                String value = decimalFormat.format(result);
                return value;
            }

        }
        catch (Exception e)
        {
            return "INVALID";
        }
        DecimalFormat twoDForm = new DecimalFormat("#.#########");

        try {
            result= Double.valueOf(twoDForm.format(result));
        }
        catch (Exception e)
        {
            return "INVALID";
        }

        if(String.valueOf(result).equals("NaN"))
        {
            return "INVALID";
        }


        return String.valueOf(result);
        //return new BigDecimal(String.valueOf(result)).setScale(4, BigDecimal.ROUND_HALF_UP).toString();

    }

    //sent to output
    public void sentToOutput(String value,String op,boolean flag)
    {
        if(input.getText().toString().equals("-")||input.getText().toString().equals("INVALID"))
        {
            return;
        }

        resetInput=true;

        if(flag==false)
        {

            //if first no
            if (num1==null)
            {


                if(op!="=")
                {
                    num1=value;
                    previousOp = op;

                    output.append(value);
                    output.append(op);
                }
                else
                {
                    resetInput=false;
                }

            }


            //if second number
            else
            {
                num2=value;

                String tempNum2=num2;

                String result=calculate(num1, num2,previousOp);
                input.setText(result);

                num1=result;
                num2=null;


                if(op!="=")
                {
                    previousOp = op;


                    output.append(value);
                    output.append(op);

                }
                else
                {
                    previousOp=null;
                    num1=null;

                    //sent history

                    if(String.valueOf(history.getText()).equals("Do some calculations and they will appear here !"))
                    {


                       history.setGravity(Gravity.LEFT);
                        history.setText("");
                    }
                    history.append(output.getText().toString()+tempNum2+" =");
                    history.append(input.getText().toString()+"\n");
                    output.setText("");
                }

            }


        }

        //if again operator pressed
        else
        {

            if(op!="=")
            {
                previousOp = op;

                StringBuffer text = new StringBuffer(String.valueOf(output.getText()));


                int length = text.length();
                text.deleteCharAt(length - 1);
                text.deleteCharAt(length - 2);
                text.deleteCharAt(length - 3);
                text.append(op);

                output.setText(text);
            }

        }

        if(op!="=")
        {
            opAgain = true;
        }


    }

    @Override
    public void onClick(View v)
    {



        opAgain=false;
        switch (v.getId())
        {
            case R.id.num0:


                if(input.getText().toString().equals("0"))
                {


                }
                else
                {

                    if(resetInput)
                    {
                        input.setText("0");
                    }
                    else
                    {
                        input.append("0");
                    }
                }
                resetInput=false;

                break;

            case R.id.num1:

                if(input.getText().toString().equals("0"))
                {
                input.setText("1");
                }

                else
                {
                    if(resetInput)
                    {
                        input.setText("1");
                    }
                    else
                    {
                        input.append("1");
                    }
                }
                resetInput=false;

                break;

            case R.id.num2:

                if(input.getText().toString().equals("0"))
                {
                    input.setText("2");
                }

                else
                {
                    if(resetInput)
                    {
                        input.setText("2");
                    }
                    else
                    {
                        input.append("2");
                    }
                }
                resetInput=false;

                break;

            case R.id.num3:
                if(input.getText().toString().equals("0"))
                {
                    input.setText("3");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("3");
                    }
                    else
                    {
                        input.append("3");
                    }
                }
                resetInput=false;
                break;

            case R.id.num4:
                if(input.getText().toString().equals("0"))
                {
                    input.setText("4");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("4");
                    }
                    else
                    {
                        input.append("4");
                    }
                }
                resetInput=false;
                break;

            case R.id.num5:

                if(input.getText().toString().equals("0"))
                {
                    input.setText("5");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("5");
                    }
                    else
                    {
                        input.append("5");
                    }
                }
                resetInput=false;
                break;

            case R.id.num6:
                if(input.getText().toString().equals("0"))
                {
                    input.setText("6");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("6");
                    }
                    else
                    {
                        input.append("6");
                    }
                }
                resetInput=false;
                break;


            case R.id.num7:
                if(input.getText().toString().equals("0"))
                {
                    input.setText("7");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("7");
                    }
                    else
                    {
                        input.append("7");
                    }
                }
                resetInput=false;
                break;

            case R.id.num8:
                if(input.getText().toString().equals("0"))
                {
                    input.setText("8");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("8");
                    }
                    else
                    {
                        input.append("8");
                    }
                }
                resetInput=false;
                break;

            case R.id.num9:
                if(input.getText().toString().equals("0"))
                {
                    input.setText("9");
                }
                else
                {
                    if(resetInput)
                    {
                        input.setText("9");
                    }
                    else
                    {
                        input.append("9");
                    }
                }
                resetInput=false;
                break;

            case R.id.numDot:
                boolean dotFlag=false;

                if(resetInput)
                {
                    input.setText("0.");
                    resetInput=false;
                    return;
                }

                String text=input.getText().toString();
                for(int i=0;i<text.length();i++)
                {
                    if(text.charAt(i)=='.')
                    {
                        dotFlag=true;
                        break;
                    }
                }


                if(!dotFlag)
                {
                    if(input.getText().toString().equals("0"))
                    {
                        input.setText("0.");
                    }

                    else
                    {
                        if(resetInput)
                        {
                            input.setText("0.");
                        }
                        else
                        {
                            input.setText(input.getText()+".");
                        }
                    }
                    resetInput=false;
                }

                break;

        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref= PreferenceManager.getDefaultSharedPreferences(getContext());

    }
}
