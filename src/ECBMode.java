import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ECBMode implements IModeCipher
{
    private final ISymmetricalCipher symmetricalAlgorithm;
    private final int processors;

    public ECBMode(ISymmetricalCipher symmetricalAlgorithm)
    {
        this.symmetricalAlgorithm = symmetricalAlgorithm;
        this.processors = Runtime.getRuntime().availableProcessors();
    }

    @Override
    public byte[] encrypt(byte[] notCipherText)
    {
        ExecutorService service = Executors.newFixedThreadPool(processors);
        List<Future<byte[]>> encryptedBlocksFutures = new LinkedList<>();

        for (int i = 0; i < notCipherText.length; i += 8)
        {
            byte[] block = HelpFunction.getArray64(notCipherText, i);
            encryptedBlocksFutures.add(service.submit(() -> symmetricalAlgorithm.encrypt(block)));
        }
        service.shutdown();
        return getArrayFromExecutors(encryptedBlocksFutures, notCipherText.length);
    }

    @Override
    public byte[] decrypt(byte[] cipherText)
    {
        ExecutorService service = Executors.newFixedThreadPool(processors);
        List<Future<byte[]>> encryptedBlocksFutures = new LinkedList<>();
        for (int i = 0; i <  cipherText.length; i += 8){
            byte[] block = HelpFunction.getArray64(cipherText, i);
            encryptedBlocksFutures.add(service.submit(() -> symmetricalAlgorithm.decrypt(block)));
        }
        service.shutdown();
        cipherText = getArrayFromExecutors(encryptedBlocksFutures, cipherText.length);
        return cipherText;
    }
    private static byte[] getArrayFromExecutors(List<Future<byte[]>> encryptedBlocksFutures, int lengthOfText)
    {
        byte[] result = new byte[lengthOfText];
        int i = 0;
        try {
            for (var futureBufToWrite : encryptedBlocksFutures)
            {
                byte[] encrypted = futureBufToWrite.get();
                System.arraycopy(encrypted, 0, result, i, 8);
                i += 8;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void reset() { }
}
