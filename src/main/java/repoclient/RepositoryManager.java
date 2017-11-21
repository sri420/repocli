package repoclient;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RepositoryManager {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final String AUTH_HEADER = "Authorization";

	public static void main(String[] args) throws IOException {

	}

	public static Response createRepository(RepoCreationRequest repoCreateRequest, String url, String encodedString)
			throws IOException {
		OkHttpClient client = new OkHttpClient();

		Gson gson = new Gson();
		String jsonInString = gson.toJson(repoCreateRequest);

		RequestBody body = RequestBody.create(JSON, jsonInString);

		Request request = new Request.Builder().header(AUTH_HEADER, encodedString).url(url).post(body).build();

		Response response = client.newCall(request).execute();
		return response;
	}
}
