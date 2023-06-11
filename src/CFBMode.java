import java.awt.*;

public class CFBMode implements IModeCipher
{
    private ISymmetricalCipher symmetricalAlgorithm;
    private byte[] initializationVector;
    private byte[] prevBlock;

    public CFBMode(ISymmetricalCipher c, byte[] IV)
    {
        symmetricalAlgorithm = c;
        initializationVector = IV;
        prevBlock = IV;
    }


    @Override
    public byte[] encrypt(byte[] notCipherText)
    {
        reset();
        byte[] prevBlockNew = new byte[prevBlock.length];
        System.arraycopy(prevBlock, 0, prevBlockNew, 0, prevBlock.length);
        try {
            for (int i = 0; i < notCipherText.length; i += 8)
            {
                var encrypt = symmetricalAlgorithm.encrypt(prevBlock);
                this.prevBlock = HelpFunction.XORByteArray(encrypt, HelpFunction.getArray64(notCipherText, i));
                System.arraycopy(this.prevBlock, 0, notCipherText, i, 8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.initializationVector = prevBlockNew;
        return notCipherText;
    }

    @Override
    public byte[] decrypt(byte[] cipherText)
    {
        reset();
        var encrypt = symmetricalAlgorithm.encrypt(prevBlock);
        prevBlock = encrypt;

        for (int i = 0; i < cipherText.length; i += 8)
        {
            byte[] block = HelpFunction.getArray64(cipherText, i);
            System.arraycopy(HelpFunction.XORByteArray(prevBlock, block), 0, cipherText, i, 8);
            prevBlock = block;
            encrypt = symmetricalAlgorithm.encrypt(prevBlock);
            prevBlock = encrypt;
        }
        return cipherText;
    }

    @Override
    public void reset()
    {
        this.prevBlock = this.initializationVector;
    }
}
