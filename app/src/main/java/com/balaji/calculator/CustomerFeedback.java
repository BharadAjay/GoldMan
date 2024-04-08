package com.balaji.calculator;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerFeedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFeedback extends Fragment {
    BillFragment billFragment = null;
    ImageView ivBack;
    GifImageView imgWorst, imgNotGood, imgFine, imgLookGood, imgVeryGood;
    Slider sliderFeedback;
    TextView tvQuestion;
    MaterialButton btnNextQuestion;
    MaterialButton btnSubmit, btnDialog;
    List<String> questions = new ArrayList<String>();
    View customDialogView;
    AlertDialog alertDialog;
    Map<String, String> feedbackResult = new HashMap<String, String>();
    EditText edtCustomerNameFeedback;
    int q = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerFeedback() {
        // Required empty public constructor
    }

    public CustomerFeedback(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerReview.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerFeedback newInstance(String param1, String param2) {
        CustomerFeedback fragment = new CustomerFeedback();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_feedback, container, false);
        customDialogView = inflater.inflate(R.layout.dialog_customer_feedback, container, false);

//        animatedImg = view.findViewById(R.id.animatedImg);
        ivBack = view.findViewById(R.id.ivBack);
        imgWorst = view.findViewById(R.id.imgWorst);
        imgNotGood = view.findViewById(R.id.imgNotGood);
        imgFine = view.findViewById(R.id.imgFine);
        imgLookGood = view.findViewById(R.id.imgLookGood);
        imgVeryGood = view.findViewById(R.id.imgVeryGood);
        sliderFeedback = view.findViewById(R.id.sliderFeedback);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        btnNextQuestion = view.findViewById(R.id.btnNextQuestion);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        edtCustomerNameFeedback = view.findViewById(R.id.edtCustomerNameFeedback);

        btnDialog = customDialogView.findViewById(R.id.btnDialog);

        questions.add("Do you liked the services and the jewellery designs which are available in our store?");
        questions.add("How frequently do you purchase jewellery?");
        questions.add("Were do you prefer purchasing jewellery?");
        questions.add("Why do you prefer branded showroom for your jewellery purchase?");
        questions.add("Which service you like the most from shop?");
        questions.add("Which?");

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
        materialAlertDialogBuilder.setView(customDialogView)
                .setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)))
                .setCancelable(false);

        tvQuestion.setText(questions.get(q));


        btnNextQuestion.setOnClickListener(v -> {
            feedbackResult.put(String.valueOf(q), String.valueOf(sliderFeedback.getValue()));

            q++;
            if (q < questions.size() - 1) {
                tvQuestion.setText(questions.get(q));
                sliderFeedback.setValue(1);
            } else {
                tvQuestion.setText(questions.get(q));
                sliderFeedback.setValue(1);
                btnNextQuestion.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        });

        btnDialog.setOnClickListener(v -> {
            getActivity().onBackPressed();
            alertDialog.dismiss();
        });

        btnSubmit.setOnClickListener(v -> {
            if (!edtCustomerNameFeedback.getText().toString().equals("")) {
                feedbackResult.put(String.valueOf(q), String.valueOf(sliderFeedback.getValue()));
                feedbackResult.put("Customer Name", edtCustomerNameFeedback.getText().toString());

                sendDataToServer(feedbackResult.toString());
                alertDialog = materialAlertDialogBuilder.show();
            } else
                Toast.makeText(getContext(), "Please enter customer name", Toast.LENGTH_SHORT).show();
        });

        ivBack.setOnClickListener(v -> getActivity().onBackPressed());

        sliderFeedback.addOnChangeListener((slider, value, fromUser) -> {
            imgWorst.setImageResource(R.drawable.disappointed_face_3d);
            imgNotGood.setImageResource(R.drawable.slightly_frowning_face_3d);
            imgFine.setImageResource(R.drawable.neutral_face_3d);
            imgLookGood.setImageResource(R.drawable.grinning_face_3d);
            imgVeryGood.setImageResource(R.drawable.smiling_face_with_heart_eyes_3d);

            if (value == 1.0) {
                imgWorst.setImageResource(R.drawable.disappointed_face);
            } else if (value == 2.0) {
                imgNotGood.setImageResource(R.drawable.slightly_frowning_face);
            } else if (value == 3.0) {
                imgFine.setImageResource(R.drawable.neutral_face);
            } else if (value == 4.0) {
                imgLookGood.setImageResource(R.drawable.grinning_face);
            } else {
                imgVeryGood.setImageResource(R.drawable.smiling_face_with_heart_eyes);
            }
        });

        return view;
    }

    private void sendDataToServer(String feedback) {
        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {
                                billFragment.socketTask.getTcpClient().sendMessageGetUTF("CUSTOMERFEEDBACK:" + feedback);
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                }).start();
            }
        } else
            Toast.makeText(getActivity(), "Please check wifi connection", Toast.LENGTH_SHORT).show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }
}