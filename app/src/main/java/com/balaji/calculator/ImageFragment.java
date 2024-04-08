package com.balaji.calculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.balaji.calculator.helper.OnSocketError;
import com.balaji.calculator.helper.SocketStatus;
import com.balaji.calculator.qrcode.QrScanActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.util.concurrent.ListenableFuture;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ImageFragment extends Fragment {

    CoordinatorLayout coLayoutImage;
    Snackbar snackbar, snackbarUploading;
    MaterialButton btnSave;
    CardView imageCard;
    Uri uri;
    ImageView imageView, ivBack;
    Intent intentSetting;
    String messageFromSeriver = null;
    private OnSocketError onSocketError;
    BillFragment billFragment = null;
    PreviewView previewViewImage;
    FloatingActionButton fabShutter;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    View dialog_image_capture_layout;
    MaterialAlertDialogBuilder materialAlertDialogBuilderImage;
    AlertDialog alertDialogImage;
    ConstraintLayout clImage;
    MaterialCardView mcvCameraDialogImage, mcvGalleryDialogImage;
    private static final String TAG = "HomeFragment";
    TextInputLayout edtItemTagNoImage;
    LinearProgressIndicator lpiImage;

    public ImageFragment() {

    }

    public ImageFragment(BillFragment billFragment) {
        this.billFragment = billFragment;
    }

    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    ActivityResultLauncher<ScanOptions> qrScanActivityResultLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    lpiImage.setVisibility(View.VISIBLE);
                    snackbarUploading = Snackbar.make(getContext(), getView(), "Image is uploading...", Snackbar.LENGTH_INDEFINITE);
                    snackbarUploading.show();
                    edtItemTagNoImage.getEditText().setText(result.getContents());
                    processQrCode();
                }
            }
    );

    private ActivityResultLauncher<Intent> imagePickerActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result != null) {
            if (result.getData() != null) {
                uri = result.getData().getData();

                btnSave.setEnabled(true);
                edtItemTagNoImage.getEditText().setEnabled(true);
                imageView.setImageURI(null);
                imageView.setImageURI(uri);
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image, container, false);
//        ButterKnife.bind(this, view);
//        billFragment.allowDisconnect = false;

        edtItemTagNoImage = view.findViewById(R.id.edtItemTagNoImage);
        imageView = view.findViewById(R.id.ibTag);
        imageCard = view.findViewById(R.id.imageCard);
        btnSave = view.findViewById(R.id.btnSave);
        intentSetting = new Intent(getContext(), SettingActivity.class);
        ivBack = view.findViewById(R.id.ivBack);
        coLayoutImage = view.findViewById(R.id.coLayoutImage);
        clImage = view.findViewById(R.id.clImage);
        previewViewImage = view.findViewById(R.id.previewViewImage);
        fabShutter = view.findViewById(R.id.fabShutter);
        lpiImage = view.findViewById(R.id.lpiImage);

        //dialog
        materialAlertDialogBuilderImage = new MaterialAlertDialogBuilder(getContext());
        dialog_image_capture_layout = inflater.inflate(R.layout.dialog_image_capture_layout, container, false);
        materialAlertDialogBuilderImage.setView(dialog_image_capture_layout);
        alertDialogImage = materialAlertDialogBuilderImage.create();
        mcvCameraDialogImage = dialog_image_capture_layout.findViewById(R.id.mcvCameraDialogImage);
        mcvGalleryDialogImage = dialog_image_capture_layout.findViewById(R.id.mcvGalleryDialogImage);

        edtItemTagNoImage.setEndIconOnClickListener(v -> {
            if (isImageSelected()) {
                try {
                    billFragment.allowDisconnect = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ScanOptions options = new ScanOptions();
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(QrScanActivity.class);
                qrScanActivityResultLauncher.launch(options);
            }
        });

        onSocketError = error -> {
            getActivity().runOnUiThread(() -> {
//                setStatus(SocketStatus.ERROR);
//                showSnackBar(error.getLocalizedMessage());
            });
        };

        imageView.setOnClickListener(v -> {
            alertDialogImage.show();
        });

        btnSave.setEnabled(false);
        btnSave.setOnClickListener(v -> {
            if (!(edtItemTagNoImage.getEditText().getText().toString().equals(""))) {
                lpiImage.setVisibility(View.VISIBLE);
                snackbarUploading = Snackbar.make(getContext(), getView(), "Image is uploading...", Snackbar.LENGTH_INDEFINITE);
                snackbarUploading.show();
                processQrCode();
            } else
                edtItemTagNoImage.setError("Please enter tag no");
        });

        edtItemTagNoImage.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                lpiImage.setVisibility(View.VISIBLE);
                snackbarUploading = Snackbar.make(getContext(), getView(), "Image is uploading...", Snackbar.LENGTH_INDEFINITE);
                snackbarUploading.show();
                edtItemTagNoImage.clearFocus();
                processQrCode();

                return true;
            }
            return false;
        });

        mcvCameraDialogImage.setOnClickListener(v -> {
            imageView.setVisibility(View.GONE);
            clImage.setVisibility(View.VISIBLE);
            startCamera(cameraFacing);
            alertDialogImage.dismiss();
        });

        mcvGalleryDialogImage.setOnClickListener(v -> {
            pickImage();
            alertDialogImage.dismiss();
        });

        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        return view;
    }

    void startCamera(int cameraFacing) {
//        int aspectRatio = aspectRatio(previewViewImage.getWidth(), previewViewImage.getHeight());
        int aspectRatio = AspectRatio.RATIO_4_3;
        ListenableFuture listenableFuture = ProcessCameraProvider.getInstance(getContext());
        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();
                Preview preview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build();
                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetResolution(new Size(900, 900))
                        .setTargetRotation(Surface.ROTATION_0).build();
//                        .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation()).build();
                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(cameraFacing).build();
                cameraProvider.unbindAll();
                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                fabShutter.setOnClickListener(v -> takePicture(imageCapture));

                preview.setSurfaceProvider(previewViewImage.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    public void takePicture(ImageCapture imageCapture) {
        final File file = new File(getActivity().getFilesDir(), "Tag_Image.jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.shutter_sound);
                mediaPlayer.start();

                getActivity().runOnUiThread(() -> {
                    imageView.setVisibility(View.VISIBLE);
                    clImage.setVisibility(View.GONE);
                    uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", file);
                    btnSave.setEnabled(true);
                    edtItemTagNoImage.getEditText().setEnabled(true);
                    imageView.setImageURI(null);
                    imageView.setImageURI(uri);
                });
                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                });
                startCamera(cameraFacing);
            }
        });
    }

    void pickImage() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            intent.setType("image/*");
            imagePickerActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processQrCode() {
        if (edtItemTagNoImage.getEditText().getText().toString().trim() == null || edtItemTagNoImage.getEditText().getText().toString().isEmpty()) {
            return;
        }

        if (SecurePreferences.getStringPreference(getActivity(), "ip").isEmpty()) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
            Toast.makeText(getActivity(), "Please set ip first.", Toast.LENGTH_SHORT).show();
            return;
        }

        String tagNo = edtItemTagNoImage.getEditText().getText().toString();

        if (haveNetworkConnection()) {
            if (billFragment.socketTask != null) {
                new Thread(() -> {
                    // Run whatever background code you want here.
                    if (billFragment.socketTask.getTcpClient() != null) {
                        if (billFragment.socketTask.getTcpClient().getSocket() != null) {
                            if (billFragment.socketTask.getTcpClient().getSocket().isConnected()) {

                                String response = "";
                                String s = encode();
                                int totalLength = s.length();
                                int index = 50000;
                                if (totalLength <= index) {
                                    messageFromSeriver = null;
                                    billFragment.socketTask.getTcpClient().sendMessageGetUTF("SIMG:" + tagNo + ":" + s);
                                } else {
                                    boolean flag = true;
                                    int start = 0;
                                    while (flag) {
                                        int end = start + index;
                                        String s1 = s.substring(start, end);
                                        messageFromSeriver = null;
                                        billFragment.socketTask.getTcpClient().sendMessageGetUTF("SIMG:" + tagNo + ":" + s1);

                                        String msg = messageFromSeriver;
                                        start = end;
                                        if ((end + index) >= totalLength) {
                                            s1 = s.substring(start, (totalLength - 1));
                                            messageFromSeriver = null;
                                            billFragment.socketTask.getTcpClient().sendMessageGetUTF("SIMG:" + tagNo + ":" + s1);
                                            msg = messageFromSeriver;
                                            response = msg;
                                            flag = false;
                                        } else {
                                            if ((start + index) > totalLength) {
                                                index = (start + index) - (totalLength - 1);
                                            }
                                            Log.d(TAG, "MSG " + msg);
                                            Log.d(TAG, "index " + index);
                                            Log.d(TAG, "start " + start);
                                            Log.d(TAG, "total " + totalLength);

                                        }
                                    }
                                }
                            } else {
                                setStatus(SocketStatus.DISCONNECTED);
                            }
                        } else {
                            setStatus(SocketStatus.DISCONNECTED);
                        }
                    } else {
                        setStatus(SocketStatus.DISCONNECTED);
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

    String encode() {
        if (isImageSelected()) {
            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uri);

                Bitmap image = BitmapFactory.decodeStream(inputStream, null, null);

                // Encode image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return imageString;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "";
            }
        }
        return null;
    }

    boolean isImageSelected() {
        if (uri == null) {
            Toast.makeText(getContext(), "Please choose image first", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void snackbarMessage(String msg) {
        if (msg.equals("SAVE")) {
            snackbarUploading.dismiss();
            edtItemTagNoImage.getEditText().setText("");
            btnSave.setEnabled(false);
            edtItemTagNoImage.getEditText().setEnabled(false);
            imageView.setImageResource(R.drawable.bitmap);

            Snackbar.make(getContext(), getView(), "Image saved successfully.", Snackbar.LENGTH_SHORT).show();
        } else if (msg.equals("Tag Not Found")) {
            Snackbar.make(getContext(), getView(), "Tag not found.", Snackbar.LENGTH_SHORT).show();
        } else {
            snackbarUploading.dismiss();
            Snackbar.make(getContext(), getView(), "Image not saved!", Snackbar.LENGTH_SHORT)
                    .setAction("Retry", v -> {
                        snackbarUploading = Snackbar.make(getContext(), getView(), "Image is uploading...", Snackbar.LENGTH_INDEFINITE);
                        snackbarUploading.show();
                        processQrCode();
                    }).show();
        }
        lpiImage.setVisibility(View.GONE);
    }

    void setStatus(SocketStatus status) {
        snackbar = Snackbar.make(coLayoutImage, "", Snackbar.LENGTH_INDEFINITE);

        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
        Button btnActSnackbar = customSnackView.findViewById(R.id.btnActSnackbar);
        TextView tvMsgSnackbar = customSnackView.findViewById(R.id.tvMsgSnackbar);
        CardView cardView = customSnackView.findViewById(R.id.cvBgSnackbar);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        snackbarLayout.addView(customSnackView, 0);

        btnActSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnActSnackbar.getText().toString().equals("RETRY")) {
                    billFragment.connectSocket();
                } else if (btnActSnackbar.getText().toString().equals("SET")) {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
                }
            }
        });

        switch (status) {
            case ERROR:
                if (!(SecurePreferences.getStringPreference(getActivity(), "ip").isEmpty()) || SecurePreferences.getIntegerPreference(getActivity(), "port") != 0) {
                    btnActSnackbar.setText("RETRY");
                    tvMsgSnackbar.setText("Error");
                } else {
                    btnActSnackbar.setText("SET");
                    tvMsgSnackbar.setText("Please set ip address and port first");
                }
                cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_negative));
                snackbar.show();
                break;

            case CONNECTED:
                btnActSnackbar.setEnabled(false);
                tvMsgSnackbar.setText("Online");
                snackbar.setDuration(Snackbar.LENGTH_SHORT);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_positive));
                snackbar.show();
                break;

            case CONNECTING:
                btnActSnackbar.setEnabled(false);
                tvMsgSnackbar.setText("Connecting");
                cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_warning));
                snackbar.show();
                break;

            case DISCONNECTED:
                btnActSnackbar.setText("RETRY");
                tvMsgSnackbar.setText("Please refresh connection");
                cardView.setCardBackgroundColor(getResources().getColor(R.color.clr_negative));
                snackbar.show();
                break;

            default:
                break;
        }
    }
}
