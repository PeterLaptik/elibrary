package pl.controllers;

import javax.ejb.LocalBean;
import javax.inject.Singleton;

@LocalBean
@Singleton
public class RoutesKeeper {
	public final static String PAGE_LOGIN = "login";
	public final static String PAGE_HOME = "home";
}
