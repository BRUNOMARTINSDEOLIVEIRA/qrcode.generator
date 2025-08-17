package com.brunomartinsdeoliveira.qrcode.generator.service;

import com.brunomartinsdeoliveira.qrcode.generator.ports.StoragePort;
import com.brunomartinsdeoliveira.qrcode.generator.dto.QrCodeGeneratorResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class QrCodeGeneratorService {

    private final StoragePort storage;

    public QrCodeGeneratorService(StoragePort storage) {
        this.storage = storage;
    }

    public QrCodeGeneratorResponse generateAndUploadQrCode(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngQrCodeData = pngOutputStream.toByteArray();

        String fileName = UUID.randomUUID().toString();
        String url = storage.uploadFile(pngQrCodeData, fileName, "image/png");

        return new QrCodeGeneratorResponse(url);
    }
}
