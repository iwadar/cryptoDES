
public class CBCMode implements IModeCipher
{
    private ISymmetricalCipher symmetricalAlgorithm;
    private byte[] initializationVector;
    private byte[] prevBlock;

    public CBCMode(ISymmetricalCipher c, byte[] IV)
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
                byte[] block = HelpFunction.getArray64(notCipherText, i);
                block = HelpFunction.XORByteArray(block, prevBlock);
                prevBlock = symmetricalAlgorithm.encrypt(block);
                System.arraycopy(prevBlock, 0, notCipherText, i, 8);
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
        byte[] blockSave = new byte[8];
        try {
            for (int i = 0; i < cipherText.length; i += 8)
            {
                byte[] block = HelpFunction.getArray64(cipherText, i);
                System.arraycopy(block, 0, blockSave, 0, block.length);
                var decrypt = symmetricalAlgorithm.decrypt(block);
                System.arraycopy(HelpFunction.XORByteArray(prevBlock, decrypt), 0, cipherText, i, 8);
                System.arraycopy(blockSave, 0, prevBlock, 0, blockSave.length);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cipherText;
    }
    @Override
    public void reset()
    {
        this.prevBlock = this.initializationVector;
    }
}
