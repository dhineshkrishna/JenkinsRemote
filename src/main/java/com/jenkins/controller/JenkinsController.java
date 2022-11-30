package com.jenkins.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JenkinsController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 117f939033043ecf04dd84d9d367657753 == Token

	// "http://localhost:8080/job/docker-jenkins-integration/build?token=sivalingam"

	// String JENKINS_URL =
	// "http://localhost:8080/api/json?tree=jobs[name,buildable]&pretty";
	@GetMapping("/jenkinspage")
	public String scrape(String urlString, String username, String password, Model model)
			throws ClientProtocolException, IOException {
		urlString = "http://localhost:8081/api/json?tree=jobs[name]";
		username = "dhineshkrishna";
		password = "Dhinesh@123";

		URI uri = URI.create(urlString); // Convert String to URL

		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));

		AuthCache authCache = new BasicAuthCache(); // Create AuthCache instance

		BasicScheme basicAuth = new BasicScheme(); // Generate BASIC scheme object and add it to the local auth cache

		authCache.put(host, basicAuth);

		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		HttpGet httpGet = new HttpGet(uri); // Add AuthCache to the execution context

		HttpClientContext localContext = HttpClientContext.create();

		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);

		String xy = EntityUtils.toString((response.getEntity()));

		JSONObject result = new JSONObject(xy); // Convert String to JSON Object

		JSONArray fromJsonArray = result.getJSONArray("jobs");
		
		System.out.println("**********"+fromJsonArray);
		List<Object> lc = fromJsonArray.toList();
		model.addAttribute("fromJsonArray",lc);
		
		 return "job";

	}

	

}
