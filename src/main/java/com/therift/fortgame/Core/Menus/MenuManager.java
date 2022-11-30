package com.therift.fortgame.Core.Menus;

import com.therift.fortgame.Core.Menus.Commands.FortCommand;
import com.therift.fortgame.Core.Menus.FortMenus.FortSelectMenu;
import com.therift.fortgame.Main;

public class MenuManager {

    private FortSelectMenu fortSelectMenu;
    private Main main;

    public MenuManager(Main main){

        this.main = main;

        //-----------------------------------
        //      Menu Classes
        //-----------------------------------
        fortSelectMenu = new FortSelectMenu(main);

        //-----------------------------------
        //      Menu Commands
        //-----------------------------------
        main.getCommand("fort").setExecutor(new FortCommand());

    }

    public FortSelectMenu getFortSelectMenu() {
        return fortSelectMenu;
    }
}
