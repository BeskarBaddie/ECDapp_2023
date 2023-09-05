package com.honours.ecd_2023;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FullscreenVideoUnitTest {
    @Mock
    private OkHttpClient mockOkHttpClient;

    @Mock
    private Call mockCall;

    private FullscreenVideo fullscreenVideo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fullscreenVideo = new FullscreenVideo();
        fullscreenVideo.okHttpClient = mockOkHttpClient;
    }

    @Test
    public void testStartDownloading_Success() throws IOException {
        // Arrange
        String downloadUrl = "http://example.com/video.mp4";
        String title = "TestVideo";
        when(mockOkHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mock(Response.class));
        when(mockCall.enqueue(any(Callback.class))).thenAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mock(Response.class));
            return null;
        });

        // Act
        fullscreenVideo.startDownloading(downloadUrl, title);

        // Assert
        // Add your assertions based on the expected behavior
        // For example, you can verify that Toast.makeText was called with expected parameters
    }

    @Test
    public void testStartDownloading_Failure() throws IOException {
        // Arrange
        String downloadUrl = "http://example.com/video.mp4";
        String title = "TestVideo";
        when(mockOkHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mock(Response.class));
        when(mockCall.enqueue(any(Callback.class))).thenAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new IOException("Download failed"));
            return null;
        });

        // Act
        fullscreenVideo.startDownloading(downloadUrl, title);

        // Assert
        // Add your assertions based on the expected behavior
        // For example, you can verify that Toast.makeText was called with expected parameters
    }



}
