package com.mouse.lion.pocketdj.business;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.common.io.BaseEncoding;
import com.mouse.lion.pocketdj.R;
import com.mouse.lion.pocketdj.utils.Logger;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lionm on 1/21/2018.
 */

public class YoutubeApiClient {

    public static class MetaData {

        public final String title;
        public final String thumbnailUrl;
        public final String pageUrl;

        public MetaData(String title, String thumbnailUrl, String pageUrl) {
            this.title = title;
            this.thumbnailUrl = thumbnailUrl;
            this.pageUrl = pageUrl;
        }
    }

    public interface OnMetaDataLoadingResult {
        void onMetaDataLoaded(MetaData metaData);
        void onLoadingMetaDataFailed();
    }

    private static final String API_KEY = "AIzaSyDxfRljsYKI3BLxyGCanIOqRSNOc5scUiQ";
    private static final String REGEX_VIDEO_ID_IN_URL = "https://www.youtube.com/watch\\?v=(.+)";
    private static final Pattern PATTERN_VIDEO_ID_IN_URL = Pattern.compile(REGEX_VIDEO_ID_IN_URL);

    private final YouTube YOUTUBE;

    public YoutubeApiClient(final Context context) {

        YOUTUBE = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                // without the following codes, android system will causes the error:
                //
                // 'There was a service error: 403 : The request did not specify any Android package name
                // or signing-certificate fingerprint. Please ensure that the client is sending them or use
                // the API Console to update your key restrictions.'
                //
                // i fond the solution in this site -> https://stackoverflow.com/questions/39543105/youtube-api-key
                String SHA1 = getSHA1(context);
                request.getHeaders().set("X-Android-Package", context.getPackageName());
                request.getHeaders().set("X-Android-Cert",SHA1);
            }
        }
        ).setApplicationName(context.getResources().getText(R.string.app_name).toString()).build();
    }

    private String getSHA1(Context context) {
        try {
            // TODO; is this safe?
            Signature[] signatures = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            for (Signature signature: signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                return BaseEncoding.base16().encode(md.digest());
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e);
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e);
        }
        return null;
    }

    public void getVideoMetaDataFromUrl(String url, OnMetaDataLoadingResult onMetaDataLoadingResult) {
        new AsyncProcess_GetVideoMetaDataFromUrl(YOUTUBE, onMetaDataLoadingResult).execute(url);
    }

    private static class AsyncProcess_GetVideoMetaDataFromUrl extends AsyncTask<String, Void, MetaData> {

        private final YouTube YOUTUBE;
        private final OnMetaDataLoadingResult ON_METADATA_LOADED;

        AsyncProcess_GetVideoMetaDataFromUrl(YouTube youTube, OnMetaDataLoadingResult onMetaDataLoadingResult) {
            YOUTUBE = youTube;
            ON_METADATA_LOADED = onMetaDataLoadingResult;
        }

        @Override
        protected MetaData doInBackground(String... strings) {
            String url = strings[0];
            String videoId = extractVideoIdFromUrl(url);
            if (videoId == null) return null;

            try {
                VideoListResponse response = YOUTUBE.videos()
                        .list("snippet")
                        .setFields("items(snippet/title,snippet/thumbnails/medium/url)")
                        .setId(videoId)
                        .setKey(API_KEY)
                        .execute();

                List<Video> retrievedVideos = response.getItems();
                if (retrievedVideos.isEmpty()) return null;
                // since a unique video id is given, it will only return 1 video
                VideoSnippet snippet = retrievedVideos.get(0).getSnippet();
                return new MetaData(snippet.getTitle(), snippet.getThumbnails().getMedium().getUrl(), url);

            } catch (IOException e) {
                Logger.e(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(MetaData metaData) {
            if (ON_METADATA_LOADED == null) return;
            if (metaData != null) ON_METADATA_LOADED.onMetaDataLoaded(metaData);
            else ON_METADATA_LOADED.onLoadingMetaDataFailed();

        }
    }

    @Nullable
    private static String extractVideoIdFromUrl(String url) {
        Matcher matcher = PATTERN_VIDEO_ID_IN_URL.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
}
