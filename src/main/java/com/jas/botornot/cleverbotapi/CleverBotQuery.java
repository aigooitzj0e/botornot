 
package com.jas.botornot.cleverbotapi;

import java.net.*;
import java.io.*;
import com.google.gson.*;

public class CleverBotQuery
{
    /* Constants */
	
	/**
	* URL String used for CleverBot connection. <b>Used internally</b>
	*/
    public static final String URL_STRING = "http://www.cleverbot.com/getreply?key=";

    /* Instance Variables */
    private String key;
    private String conversationID;
    private String phrase;
    private String response;
    private int random;

    /* Getters & Setters */
	
	/**
	* Gets the input phrase
	*
	* @return String object containing the input phrase
	*/
    public String getPhrase()
    {
        return this.phrase;
    }

	/**
	* Sets the input phrase to be send in the CleverBot query.
	*
	* @param phrase input phrase to be sent to CleverBot
	*/
    public void setPhrase(String phrase)
    {
        this.phrase = phrase;
    }

	/**
	* Gets the response from a request. Must be used after <code>sendRequest()</code>
	*
	* @return String object containing the CleverBot response
	*/
    public String getResponse()
    {
        return this.response;
    }

	/**
	* Sets the response value. Only used internally
	*
	* @param response CleverBot response
	*/
    private void setResponse(String response)
    {
        this.response = response;
    }

	/**
	* Gets the API key
	*
	* @return String object containing the API key
	*/
    public String getAPIKey()
    {
        return this.key;
    }

	/**
	* Sets the unique conversation identifier
	*
	* <p>
	* Set to empty ("") in order to start a new conversation with the CleverBot.
	* </p>
	* @param conversationID unique conversation identifier
	*/
    public void setConversationID(String conversationID)
    {
        this.conversationID = conversationID;
    }

	/**
	* Sets the random number. Only used internally
	*
	* @param number number to set
	*/
    private void setRandomNumber(int number)
    {
        this.random = number;
    }

	/**
	* Gets the random number
	*
	* <p>
	* CleverBot API queries provide a random number greater than zero and less than one thousand.
	* </p>
	* @return random number as an integer
	*/
    public int getRandomNumber()
    {
        return this.random;
    }

	/**
	* Gets conversation identifier.
	* <p>
	* This is a unique string used to identify conversation context to the CleverBot API servers.
	* This string is updated after every <code>sendRequest()</code> call.
	* </p>
	* @return String object containing the conversation identifier
	*/
    public String getConversationID()
    {
        return this.conversationID;
    }

	/**
	* Sends request to CleverBot servers. API key and phrase should be set prior to this call
	*
	* @throws IOException exception upon query failure
	*/
    public void sendRequest() throws IOException
    {
        /* Create & Format URL */
        URL url = new URL(CleverBotQuery.formatRequest(CleverBotQuery.URL_STRING, this.getAPIKey(), this.getPhrase(), this.getConversationID()));

        /* Open Connection */
        URLConnection urlConnection = url.openConnection();

        /* Read input */
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine = in.readLine();

        /* Create JSON Object */
        JsonObject jsonObject = new JsonParser().parse(inputLine).getAsJsonObject(); // convert to JSON

        /* Get params */
        this.setConversationID(jsonObject.get("cs").getAsString()); // update conversation ID
        this.setResponse(jsonObject.get("output").getAsString()); // get output
        this.setRandomNumber(Integer.parseInt(jsonObject.get("random_number").getAsString())); // get output
        in.close(); // close!
    }

	/**
	* CleverBotQuery constructor
	* <p>
	* Conversation identifer is set empty, thus calling sendRequest immediately after 
	* instantiation will create a new conversation 
	* </p>
	*
	* @param key API key (cleverbot.com/api) 
	* @param phrase input phrase
	*/
    public CleverBotQuery(String key, String phrase)
    {
        this.key = key;
        this.setConversationID("");
        this.setPhrase(phrase);
    }

    /* Helper Methods */
	/**
	* URL request formater
	*
	* @param url starting url to connect to 
	* @param key API key (cleverbot.com/api)
	* @param phrase input to be sent to CleverBot servers
	* @param conversationID unique conversation identifer
	* @return String object containing properly formatted URL
	*/
    private static String formatRequest(String url, String key, String phrase, String conversationID)
    {
        String formattedPhrase = phrase.replace(' ', '+');
        if (conversationID.equals(""))
        {
            return url + key + "&input=" + formattedPhrase + "&wrapper=Headline22JavaAPI";
        }
        else
        {
            return url + key  + "&input=" + formattedPhrase + "&wrapper=Headline22JavaAPI" + "&cs=" + conversationID + "&cb_settings_tweak1=50&cb_settings_tweak2=80&cb_settings_tweak3=40";
        }
    }
}
