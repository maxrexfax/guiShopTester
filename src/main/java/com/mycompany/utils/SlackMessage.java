/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utils;

	
import lombok.*;	
import java.io.Serializable;
/**
 *
 * @author user
 */
public class SlackMessage implements Serializable {
    public String username;
	
    public String text;
	
    public String icon_emoji;
}
