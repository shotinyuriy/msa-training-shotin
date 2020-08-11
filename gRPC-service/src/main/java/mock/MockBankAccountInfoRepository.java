package mock;

import com.shotin.grpc.model.BankAccountInfoEntity;
import com.shotin.grpc.repository.BankAccountInfoRepository;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Profile("mock")
public class MockBankAccountInfoRepository implements BankAccountInfoRepository {
    @Override
    public <S extends BankAccountInfoEntity> Mono<S> insert(S entity) {
        return Mono.empty();
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> insert(Iterable<S> entities) {
        return Flux.empty();
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> insert(Publisher<S> entities) {
        return Flux.empty();
    }

    @Override
    public <S extends BankAccountInfoEntity> Mono<S> save(S entity) {
        return Mono.empty();
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> saveAll(Iterable<S> entities) {
        return Flux.empty();
    }

    @Override
    public <S extends BankAccountInfoEntity> Flux<S> saveAll(Publisher<S> entityStream) {
        return Flux.empty();
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(UUID uuid) {
        return Mono.empty();
    }

    @Override
    public Mono<BankAccountInfoEntity> findById(Publisher<UUID> id) {
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> existsById(UUID uuid) {
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> existsById(Publisher<UUID> id) {
        return Mono.empty();
    }

    @Override
    public Flux<BankAccountInfoEntity> findAll() {
        return Flux.empty();
    }

    @Override
    public Flux<BankAccountInfoEntity> findAllById(Iterable<UUID> iterable) {
        return Flux.empty();
    }

    @Override
    public Flux<BankAccountInfoEntity> findAllById(Publisher<UUID> publisher) {
        return Flux.empty();
    }

    @Override
    public Mono<Long> count() {
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteById(Publisher<UUID> id) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(BankAccountInfoEntity entity) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends BankAccountInfoEntity> entities) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends BankAccountInfoEntity> entityStream) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteAll() {
        return Mono.empty();
    }
}
