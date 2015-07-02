package com.wickedgaminguk.tranxcraft.modules;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.NetworkUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class GeoIpModule extends Module<TranxCraft> {

    private HashMap<InetAddress, CityResponse> responseCache = new HashMap<>();
    private DatabaseReader reader;
    private File database;

    @Override
    public void onLoad() {
        database = new File(plugin.getDataFolder() + "/GeoLite2-City.mmdb");

        if (!database.exists()) {
            LoggerUtils.info(plugin, "No database found - downloading...");
            NetworkUtils.download("https://www.tranxcraft.com/downloads/GeoLite2-City.mmdb", database, false);
        }

        try {
            reader = new DatabaseReader.Builder(database).build();
        }
        catch (IOException ex) {
            DebugUtils.debug(ex);
        }
    }

    public CityResponse getInfo(InetAddress address) {
        if (responseCache.containsKey(address)) {
            return responseCache.get(address);
        }

        if (reader == null) {
            try {
                reader = new DatabaseReader.Builder(database).build();
            }
            catch (IOException ex) {
                DebugUtils.debug(ex);
            }
        }

        if (reader != null) {
            try {
                CityResponse response = reader.city(InetAddress.getByName(address.toString().substring(1)));

                responseCache.put(address, response);
                return response;
            }
            catch (IOException | GeoIp2Exception ex) {
                DebugUtils.debug(ex);
                return null;
            }
        }
        else {
            DebugUtils.debug(2, "The DatabaseReader is null.");
            return null;
        }
    }

    public String formatMessage(CityResponse response) {
        switch (response.getCountry().getName()) {
            case "United States": {
                return "The United States";
            }

            case "United Kingdom": {
                return "The United Kingdom";
            }

            case "Philippines": {
                return "The Philippines";
            }

            case "Netherlands": {
                return "The Netherlands";
            }

            case "Czech Republic": {
                return "The Czech Republic";
            }

            case "United Arab Emirates": {
                return "The United Arab Emirates";
            }

            case "Dominican Republic": {
                return "The Dominican Republic";
            }

            case "Democratic Republic of the Congo": {
                return "The Democratic Republic of the Congo";
            }

            case "Ivory Coast": {
                return "The Ivory Coast";
            }

            case "Republic of the Congo": {
                return "The Republic of the Congo";
            }

            case "Central African Republic": {
                return "Central African Republic";
            }

            case "French Polynesia": {
                return "The French Polynesia";
            }

            case "Somalia": {
                return "Somalia - Welcome on-board ye pirates!";
            }

            case "Solomon Islands": {
                return "The Solomon Islands";
            }

            case "Seychelles": {
                return "The Seychelles";
            }

            case "Faroe Islands": {
                return "The Faroe Islands";
            }

            case "Cayman Islands": {
                return "The Cayman Islands";
            }

            case "U.S. Virgin Islands": {
                return "The US Virgin Islands";
            }

            case "Micronesia, Federated States of": {
                return "The Federated States of Micronesia";
            }

            case "British Virgin Islands": {
                return "The British Virgin Islands";
            }

            case "Marshall Islands": {
                return "The Marshal Islands";
            }

            case "Falkland Islands": {
                return "The Falkland Islands";
            }

            default: {
                return response.getCountry().getName();
            }
        }
    }
}
