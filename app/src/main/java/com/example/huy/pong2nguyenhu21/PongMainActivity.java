package com.example.huy.pong2nguyenhu21;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

/**
 * PongMainActivity
 * 
 * This is the activity for the Pong game. It attaches a PongAnimator to
 * an AnimationSurface.
 * Also includes listener for the buttons
 * Enhancements:
 *      Making an AI
 *      Keep a running score
 *      Changable paddle size
 *      Button for user to indicates she is ready for the next turn
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @author Huy Nguyen
 * @version March 2018
 * 
 */
public class PongMainActivity extends Activity implements View.OnClickListener {

	//Instace of the BallAnimator class
	BallAnimator ballAnimator = new BallAnimator();
	/**
	 * creates an AnimationSurface containing a TestAnimator.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pong_main);



		// Connect the animation surface with the animator
		AnimationSurface mySurface = (AnimationSurface) this
				.findViewById(R.id.animationSurface);
		mySurface.setAnimator(ballAnimator);

		//Register the buttons
		Button smallPaddleButton = (Button)findViewById(R.id.smallButton);
		Button bigPaddleButton = (Button)findViewById(R.id.bigButton);
        Button continueButton = (Button)findViewById(R.id.continueButton);

		//Set on click listeners for the buttons
		smallPaddleButton.setOnClickListener(this);
		bigPaddleButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);





	}

	@Override
	public void onClick(View v) {
		//If Big Paddle button is clicked, change the paddle size to big
		if(v.getId() == R.id.bigButton) {
			ballAnimator.setPaddleBig();
		}
		//If Small Paddle button is clicked, change the paddle size to small
		else if(v.getId() == R.id.smallButton) {
			ballAnimator.setPaddleSmall();
		}
		//If the continue button is clicked, set isContinue to true
		if(v.getId() == R.id.continueButton){
			ballAnimator.setContinue();
		}
	}


}
