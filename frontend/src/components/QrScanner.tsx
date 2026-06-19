import { useEffect } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";

interface QrScannerProps {
    onScanSuccess: (decodedText: string) => void;
}

export default function QrScanner({ onScanSuccess }: QrScannerProps) {
    useEffect(() => {
        const scanner = new Html5QrcodeScanner(
            "qr-reader",
            {
                fps: 10,
                qrbox: 250,
            },
            false
        );

        scanner.render(
            (decodedText) => {
                onScanSuccess(decodedText);
            },
            (error) => {
                console.log(error);
            }
        );

        return () => {
            scanner.clear().catch(console.error);
        };
    }, [onScanSuccess]);

    return <div id="qr-reader"></div>;
}