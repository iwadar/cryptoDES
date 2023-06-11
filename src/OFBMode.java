
public class OFBMode implements IModeCipher
{

    private ISymmetricalCipher symmetricalAlgorithm;
    private byte[] initializationVector;
    private byte[] prevBlock;

    public OFBMode(ISymmetricalCipher c, byte[] IV)
    {
        this.symmetricalAlgorithm = c;
        this.initializationVector = IV;
        this.prevBlock = IV;
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
                prevBlock = symmetricalAlgorithm.encrypt(prevBlock);
                System.arraycopy(HelpFunction.XORByteArray(prevBlock, HelpFunction.getArray64(notCipherText, i)), 0, notCipherText, i, 8);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.initializationVector = prevBlockNew;
        return notCipherText;
    }

    @Override
    public byte[] decrypt(byte[] cipherText)
    {
        reset();
        prevBlock = symmetricalAlgorithm.encrypt(prevBlock);

        for (int i = 0; i < cipherText.length; i += 8)
        {
            byte[] block = HelpFunction.getArray64(cipherText, i);
            System.arraycopy(HelpFunction.XORByteArray(prevBlock, block), 0, cipherText, i, 8);
            prevBlock = symmetricalAlgorithm.encrypt(prevBlock);
        }
        return cipherText;
    }

    @Override
    public void reset()
    {
        this.prevBlock = this.initializationVector;
    }
}
