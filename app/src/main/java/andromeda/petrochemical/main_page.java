package andromeda.petrochemical;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class main_page extends AppCompatActivity implements View.OnClickListener {
    private Button signInbtn;
    private Button signUpbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_main);
        signInbtn = (Button)findViewById(R.id.signInMain);
        signInbtn.setOnClickListener(this);
        signUpbtn = (Button)findViewById(R.id.signUpMain);
        signUpbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInMain:
                Intent MainIntent = new Intent(this,LoginPage.class);
                startActivity(MainIntent);
                break;
            case R.id.signUpMain:
                Intent MainSignUpIntent = new Intent(this,SignUp.class);
                startActivity(MainSignUpIntent);
                break;
            default:
                break;
        }

    }
}
