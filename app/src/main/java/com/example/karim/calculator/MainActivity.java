package com.example.karim.calculator;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private String query;
    private TextView editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        query="";
    }
    protected void onClick(View view)
    {
        Button current = (Button)view;
        Button temp1 = (Button)findViewById(R.id.btnClear);
        Button temp2 = (Button)findViewById(R.id.btnDel);
        Button temp3 = (Button)findViewById(R.id.btnExit);
        Button temp4 = (Button)findViewById(R.id.btnEqu);
        editText = (TextView) findViewById(R.id.edEntry);
	    //operations buttons
        if(current.getId()!=temp1.getId()
                &&current.getId()!=temp2.getId()
                &&current.getId()!=temp3.getId()
                &&current.getId()!=temp4.getId())
        {
            String now = current.getText().toString();
            if(now.compareTo("/")==0)
                now = " / ";
            else if(now.compareTo("*")==0)
                now = " * ";
            else if(now.compareTo("+")==0)
                now = " + ";
            else if(now.compareTo("-")==0)
            {
                if(editText.getText().length()==0)
                    now="-";
                else
                {
                    if(query.charAt(query.length()-1)!=' ')
                        now = " - ";
                    else
                        now = "-";
                }
            }
            query+=now;
            editText.setText(query.toString());
        }
        //delete button
        else if(current.getId()==temp2.getId())
        {
            if(query.length()>0)
            {
                query = query.substring(0,query.length()-1);
                editText.setText(query);
            }
        }
        //clear button
        else if(current.getId()==temp1.getId())
        {
            editText.setText("");
            query = "";
        }
        //exit button
        else if(current.getId()==temp3.getId())
            finish();
        //equal button
        else if(current.getId()==temp4.getId())
            calculateOperations();
    }
    private void calculateOperations()
    {
        double [] tmps = new double[100];
        char [] op = new char[100];
        double res = 0.0;
        editText.setText(query);
        int idx = 0,cnt = 0;
        String temp = "";
        for ( ; idx < query.length(); idx++)
        {
            if(query.charAt(idx)==' ')
            {
                tmps[cnt] = Double.parseDouble(temp);
                idx++;
                op[cnt++] = query.charAt(idx++);
                temp = "";
                idx++;
            }
            temp+=query.charAt(idx);
        }
        if (temp!="")
            tmps[cnt++]=Double.parseDouble(temp);
        if(op[0] == '-')
            tmps[1] *= -1;
        for(idx = 1; idx < cnt;idx++)
            if(idx+1<cnt&&op[idx]=='-')
                tmps[idx+1]*=-1;
        Stack<Double> sNumbers = new Stack<Double>();
        sNumbers.push(tmps[0]);
        for(idx = 1;idx < cnt;idx++)
        {
            if(idx-1>=0&&op[idx-1]=='*'||op[idx-1]=='/')
            {
                switch (op[idx-1])
                {
                    case '*': res = tmps[idx]*sNumbers.lastElement(); break;
                    case '/':
                    {
                        if(tmps[idx]!=0)
                            res = sNumbers.firstElement()/tmps[idx];
                        else
                        {
                            editText.setText("Can not divide by zero!!");
                            finish();
                        }
                    }
                }
                sNumbers.pop();
                sNumbers.push(res);
            }
            else
            {
                sNumbers.push(tmps[idx]);
            }
        }
        double ans = 0.0;
        while(!sNumbers.isEmpty())
        {
            ans += sNumbers.lastElement();
            sNumbers.pop();
        }
        editText.setText("");
        if(ans - (int)ans == 0)
            editText.setText((int)ans+"");
        else
            editText.setText(ans+"");
        query = editText.getText().toString();
    }
}
