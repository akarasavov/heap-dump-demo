package atk.app;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Run
{

    public static void main( String[] args ) throws InterruptedException
    {
        if ( args.length == 0 )
        {
            throw new IllegalArgumentException( "Pass heap dump folder as an argument" );
        }

        Runtime.getRuntime().addShutdownHook( new Thread( () -> handleShutdown( Path.of( args[0] ) ) ) );

        allocateObjects();
    }

    private static void allocateObjects()
    {
        final List<byte[]> bytesArray = new ArrayList<>();
        while ( true )
        {
            bytesArray.add( new byte[Integer.MAX_VALUE] );
        }
    }

    private static void handleShutdown( Path heapDumpFolder )
    {
        final var pid = ProcessHandle.current().pid();
        try
        {
            System.out.printf( "Process=%d receive shutdown signal%n \n", pid );
            var jmapCommand = String.format( "jmap -dump:format=b,file=%s %d", heapDumpFolder.resolve( getFilename() ), pid );
            System.out.printf( "Start executing command:%s \n", jmapCommand );

            final var process = Runtime.getRuntime().exec( jmapCommand );
            final var result = process.waitFor();
            if ( result == 0 )
            {
                System.out.println( "Successfully executed " + jmapCommand );
            }
            else
            {
                System.out.println( "Fail to execute " + jmapCommand );
            }
        }
        catch ( IOException | InterruptedException e )
        {
            System.out.println( "Shutdown handler fail with " + e.toString() );
        }
    }

    private static Path getFilename()
    {
        return Path.of( "dump_" + new SimpleDateFormat( "yyyyMMddHHmm'.hprof'" ).format( new Date() ) );
    }
}
