package net.samagames.hub.common.managers;

import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.JsonConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

public abstract class AbstractManager implements EntryPoints
{
    protected final Hub hub;

    protected String fileConfigName;

    public AbstractManager(Hub hub, String filename)
    {
        this(hub);
        this.fileConfigName = filename;
    }

    public AbstractManager(Hub hub)
    {
        this.hub = hub;
        this.fileConfigName = null;
        if (!(this instanceof AbstractInteractionManager))
            this.hub.getEventBus().registerManager(this);
    }

    public void log(Level level, String message)
    {
        this.hub.getLogger().log(level, "[" + this.getClass().getSimpleName() + "] " + message);
    }

    protected JsonObject reloadConfiguration()
    {
        File configuration = new File(this.hub.getDataFolder(), fileConfigName);

        if (!configuration.exists())
        {
            try(PrintWriter writer = new PrintWriter(configuration))
            {
                configuration.createNewFile();
                writer.println("{}");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        JsonConfiguration npcsConfig = new JsonConfiguration(configuration);
        return npcsConfig.load();
    }
}
