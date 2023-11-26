package Santander.Coders.Banco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa(1L, "Alice", "12345678911", "Rua 1", "123456789", null);
        pessoa.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create a Pessoa instance")
    void createPessoa_ShouldCreateInstance() {
        assertAll(
                () -> assertNotNull(pessoa),
                () -> assertEquals(1L, pessoa.getId()),
                () -> assertEquals("Alice", pessoa.getNome()),
                () -> assertEquals("12345678911", pessoa.getCpf()),
                () -> assertEquals("Rua 1", pessoa.getEndereco()),
                () -> assertEquals("123456789", pessoa.getTelefone()),
                () -> assertFalse(pessoa.isInatived()),
                () -> assertNotNull(pessoa.getCreatedAt())
        );
    }

    @Test
    @DisplayName("Should update Pessoa properties")
    void updatePessoa_ShouldUpdateProperties() {
        Pessoa updatedPessoa = new Pessoa(1L, "Bob", "98765432100", "Rua 2", "987654321", null);
        pessoa.update(updatedPessoa);

        assertAll(
                () -> assertEquals("Bob", pessoa.getNome()),
                () -> assertEquals("98765432100", pessoa.getCpf()),
                () -> assertEquals("Rua 2", pessoa.getEndereco()),
                () -> assertEquals("987654321", pessoa.getTelefone()),
                () -> assertFalse(pessoa.isInatived()),
                () -> assertNotNull(pessoa.getCreatedAt()),
                () -> assertNotNull(pessoa.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Should mark Pessoa as inactive")
    void deletePessoa_ShouldMarkAsInactive() {
        pessoa.delete();

        assertAll(
                () -> assertTrue(pessoa.isInatived()),
                () -> assertNotNull(pessoa.getInativedAt())
        );
    }

    @Test
    @DisplayName("Should set created_at, updated_at, and inatived_at timestamps")
    void timestamps_ShouldBeSet() {
        assertAll(
                () -> assertNotNull(pessoa.getCreatedAt()),
                () -> assertNull(pessoa.getUpdatedAt()),
                () -> assertNull(pessoa.getInativedAt())
        );

        pessoa.setUpdatedAt(LocalDateTime.now());
        pessoa.update(pessoa);

        assertNotNull(pessoa.getUpdatedAt());

        pessoa.delete();

        assertNotNull(pessoa.getInativedAt());
        assertTrue(pessoa.isInatived());
    }

    @Test
    @DisplayName("Should get and set id")
    void getId() {
        assertEquals(1L, pessoa.getId());
        pessoa.setId(2L);
        assertEquals(2L, pessoa.getId());
    }

    @Test
    @DisplayName("Should get and set nome")
    void getNome() {
        assertEquals("Alice", pessoa.getNome());
        pessoa.setNome("Bob");
        assertEquals("Bob", pessoa.getNome());
    }

    @Test
    @DisplayName("Should get and set cpf")
    void getCpf() {
        assertEquals("12345678911", pessoa.getCpf());
        pessoa.setCpf("98765432100");
        assertEquals("98765432100", pessoa.getCpf());
    }

    @Test
    @DisplayName("Should get and set endereco")
    void getEndereco() {
        assertEquals("Rua 1", pessoa.getEndereco());
        pessoa.setEndereco("Rua 2");
        assertEquals("Rua 2", pessoa.getEndereco());
    }

    @Test
    @DisplayName("Should get and set telefone")
    void getTelefone() {
        assertEquals("123456789", pessoa.getTelefone());
        pessoa.setTelefone("987654321");
        assertEquals("987654321", pessoa.getTelefone());
    }

    @Test
    @DisplayName("Should get and set conta")
    void getConta() {
        assertNull(pessoa.getConta());
        Conta conta = new Conta();
        pessoa.setConta(conta);
        assertEquals(conta, pessoa.getConta());
    }

    @Test
    @DisplayName("Should check if pessoa is inactive")
    void isInatived() {
        assertFalse(pessoa.isInatived());
        pessoa.setInatived(true);
        assertTrue(pessoa.isInatived());
    }

    @Test
    @DisplayName("Should get created_at timestamp")
    void getCreatedAt() {
        assertNotNull(pessoa.getCreatedAt());
    }

    @Test
    @DisplayName("Should get and set updated_at timestamp")
    void getUpdatedAt() {
        assertNull(pessoa.getUpdatedAt());
        LocalDateTime updatedAt = LocalDateTime.now();
        pessoa.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, pessoa.getUpdatedAt());
    }

    @Test
    @DisplayName("Should get and set inatived_at timestamp")
    void getInativedAt() {
        assertNull(pessoa.getInativedAt());
        LocalDateTime inativedAt = LocalDateTime.now();
        pessoa.setInativedAt(inativedAt);
        assertEquals(inativedAt, pessoa.getInativedAt());
    }

    @Test
    @DisplayName("Should set created_at timestamp")
    void setCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        pessoa.setCreatedAt(createdAt);
        assertEquals(createdAt, pessoa.getCreatedAt());
    }

    @Test
    @DisplayName("Should set updated_at timestamp")
    void setUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        pessoa.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, pessoa.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set inatived_at timestamp")
    void setInativedAt() {
        LocalDateTime inativedAt = LocalDateTime.now();
        pessoa.setInativedAt(inativedAt);
        assertEquals(inativedAt, pessoa.getInativedAt());
    }
}