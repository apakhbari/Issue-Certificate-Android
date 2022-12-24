package com.ada.forcert.utilities;

import android.service.autofill.UserData;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;


public class Converts {

    public static byte[] base64ToByteArray(String b64String)   {
        return Base64.decode(b64String, Base64.DEFAULT);
    }

    public static String ByteArrayToBase64String(byte[] toConvert)   {
        return Base64.encodeToString(toConvert, Base64.DEFAULT);
    }

    public static byte[] stringUTF32ToBytes(String stringToConvert) throws UnsupportedEncodingException {
        byte[] a = stringToConvert.getBytes("UTF-32");
        return a;
    }

/*    public CsrContainer generateKeyPairandCertificateRequest(KeyPair keyPair, byte[] keyId,
                                                             CertificateProfile certificateProfile, UserData userData)
            throws Exception {
//        byte keyLen;
//        Log.wtf("PKI", "Step 1: the function start");
//        if(certificateProfile.getKeyLength()== 1024)
//            keyLen = 0x06;
//        else if (certificateProfile.getKeyLength() == 2048)
//            keyLen = 0x07;
//        else throw new Exception("Unknown key length");

        //byte[] fileSystem = getData(FileSystem_FileID, FileSystem_TagID);
        //List<FileInfo> fileInfos = listFiles(fileSystem);
        //List<KeyFileInfo> keyFileInfos = listKeyFiles();
        //Log.wtf("PKI", "Step 2: Read card data");


        //increaseCardCFForPutData();
        //Log.wtf("PKI", "Step 3: increaseCardCFForPutData completed");
        //cmapIndx = addRecordToCMapFile();
        //Log.wtf("PKI", "Step 4: addRecordToCMapFile completed");
        //increaseCardCFForPutData();
        //Log.wtf("PKI", "Step 5: increaseCardCFForPutData completed");
        //setFlagToCMapFile(cmapIndx, 1, (byte) 0x04);
        //Log.wtf("PKI", "Step 6: setFlagToCMapFile completed");
        //increaseCardCFForCreateFile();
        //Log.wtf("PKI", "Step 7: increaseCardCFForCreateFile completed");
        //byte[] emptyCertId = findEmptyCertificateTag(fileInfos);
        //Log.wtf("PKI", "Step 8: findEmptyCertificateTag completed");
        //keyId = findEmptyKeyFile(keyFileInfos);
        //Log.wtf("PKI", "Step 9: findEmptyKeyFile completed");
        //createFile(keyId, (byte) 0x00, keyLen);
        //Log.wtf("PKI", "Step 10: createFile completed");
        //activateSelectedFile();
        //Log.wtf("PKI", "Step 11: activateSelectedFile completed");

        //Log.wtf("PKI", "Step 12: generateKeyPair completed");
        //addRecordToKeyFile((byte) 0x00, keyLen, keyId, cmapIndx);
        //Log.wtf("PKI", "Step 13: addRecordToKeyFile completed");
        //increaseCardCFForPutData();
        //Log.wtf("PKI", "Step 14: increaseCardCFForPutData completed");
        String subject = certificateProfile.getSubjectDnFields().get("CN");
        int startIndx = StringUtils.ordinalIndexOf(subject, "[", 3);
        int endIndx = StringUtils.ordinalIndexOf(subject, "]", 3);
        String postFix = StringUtils.substring(subject, startIndx, endIndx + 1);
        //setBFlagToCMapFile();
        Log.wtf("PKI", "Step 15: before generateCSR completed");
        return new CsrContainer(generateCSR(keyPair, userData, postFix), keyId) ;
    }


    //from issue
    private String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(Common.convertB64ToByteArray(message));
        byte[] signatureBytes = signature.sign();
        return Common.convertByteArrayToBase64String(signatureBytes);
    }*/
}
