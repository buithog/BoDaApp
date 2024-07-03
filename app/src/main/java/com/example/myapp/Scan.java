package com.example.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.model.Voice;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Scan extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private BarcodeView barcodeView;
    private Button scanButton;
    private Button selectImageButton;

    private Voice voiceResult = new Voice();
    private RequestQueue requestQueue;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        requestQueue = Volley.newRequestQueue(getContext());
        gson = new Gson();

        barcodeView = view.findViewById(R.id.qr_camera_preview);
        scanButton = view.findViewById(R.id.btn_scan);
        selectImageButton = view.findViewById(R.id.btn_select_image);

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE);
        barcodeView.getCameraSettings().setAutoFocusEnabled(true);
        barcodeView.setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                barcodeView.pause();
                if (result == null) {
                    // Hiển thị thông báo khi không tìm thấy mã QR
                    Toast.makeText(getContext(), "No QR code found!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), VoiceDetailsActivity.class);
                    fetchDataVoice(result.getText());
                    intent.putExtra("id", voiceResult.getId());
                    intent.putExtra("name", voiceResult.getName());
                    intent.putExtra("description", voiceResult.getDescription());
                    intent.putExtra("thumbnails", voiceResult.getThumbnailUrl());
                    intent.putExtra("mp3Url", voiceResult.getMp3Url());
                    startActivity(intent);
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeView.resume();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        checkPermission();

        return view;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                scanQRCodeFromBitmap(bitmap); // Gọi hàm scanQRCodeFromBitmap() sau khi nhận được bitmap từ Uri
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void scanQRCodeFromBitmap(Bitmap bitmap) {
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader reader = new MultiFormatReader();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE));
        reader.setHints(hints);

        try {
            Result result = reader.decode(binaryBitmap);
            String qrCodeText = result.getText();
            if (qrCodeText == null){
                Toast.makeText(getContext(), "No QR code found!", Toast.LENGTH_SHORT).show();
            }else {
                fetchDataVoice(qrCodeText);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            // Handle the case where no QR code was found in the image
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView.resume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showBottomNavigationView();
    }
    private void fetchDataVoice(String id) {
        if (getContext() != null) {
            Log.e("id", id);
            String url = getString(R.string.baseURL) + "/voice/getvoice/" + id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                                StringBuilder sb = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line).append('\n');
                                }
                                String json = sb.toString();

                                JSONObject jsonObject = new JSONObject(json);
                                // Tiếp tục xử lý JSON như bình thường

                                Log.d("API Response", jsonObject.toString());
                                Toast.makeText(getContext(), "Data fetched successfully!", Toast.LENGTH_SHORT).show();

                                // Xử lý phản hồi JSON
                                String voiceId = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String description = jsonObject.getString("description");
                                String thumbnailsUrl = getString(R.string.baseURL) + "/voice/voicethumnails/" + voiceId;
                                String fileMp3Url = getString(R.string.baseURL) + "/voice/voicefile/" + voiceId;

                                Intent intent = new Intent(getActivity(), VoiceDetailsActivity.class);
                                intent.putExtra("id",voiceId);
                                intent.putExtra("name",name);
                                intent.putExtra("description",description);
                                intent.putExtra("thumbnails",thumbnailsUrl);
                                intent.putExtra("mp3Url",fileMp3Url);
                                startActivity(intent);
                                Log.e("url",thumbnailsUrl);
                                Log.e("des",description);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi nếu có
                            Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(stringRequest);
        }
    }



}
