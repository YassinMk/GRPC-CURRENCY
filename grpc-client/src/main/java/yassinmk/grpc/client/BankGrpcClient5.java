package yassinmk.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import yassinmk.grpc.stubs.Bank;
import yassinmk.grpc.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcClient5 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest Request= Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("USD")
                .setCurrencyTo("EUR")
                .setAmount(100)
                .build();

        StreamObserver<Bank.ConvertCurrencyRequest> performSream = asyncStub.fullCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("-----------------");
                System.out.println(convertCurrencyResponse);
                System.out.println("-----------------");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("....END");
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0 ;
            @Override
            public void run() {
                Bank.ConvertCurrencyRequest currencyRequest = Bank.ConvertCurrencyRequest.newBuilder()
                        .setAmount(Math.random() * 700)
                        .build();
                performSream.onNext(currencyRequest);
                ++counter;
                System.out.println("=>>>>>>> request +"+counter);
                if(counter == 20 ){
                    performSream.onCompleted();
                    timer.cancel();
                }
            }
            },1000,1000);
        System.out.println("........?");
        System.in.read();
        };

    }

