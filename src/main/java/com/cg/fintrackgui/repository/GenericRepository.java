package com.cg.fintrackgui.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Repositório em memória para gestão flexível de coleções de dados
public class GenericRepository<T> {

    // Lista interna para armazenar os elementos na memória RAM
    private final List<T> elements;

    // Inicializa o repositório com uma lista vazia
    public GenericRepository() {
        this.elements = new ArrayList<>();
    }
    
    // Inicializa o repositório povoando-o com uma lista inicial de elementos
    public GenericRepository(List<? extends T> initialElements) {
        this.elements = new ArrayList<>();
        if (initialElements != null) {
            this.elements.addAll(initialElements);   
        }
    }

    // Adiciona um único elemento à coleção
    public void add(T element) {
        if (element != null) {
            this.elements.add(element);            
        }
    }

    // Adiciona uma lista de elementos à coleção
    public void addAll(List<? extends T> sourceList) {
        if (sourceList != null) {
            this.elements.addAll(sourceList);      
        }
    }

    // Copia os elementos armazenados para uma lista de destino
    public void copyTo(List<? super T> destinationList) {
        if (destinationList != null) {
            destinationList.addAll(this.elements);
        }
    }

    // Retorna uma vista imutável de apenas leitura dos elementos armazenados
    public List<T> findAll() {
        return Collections.unmodifiableList(this.elements);
    }

    // Remove um elemento específico da lista
    public boolean remove(T element) {
        return this.elements.remove(element);
    }

    // Limpa todos os elementos armazenados na memória
    public void clear() {
        this.elements.clear();
    }

    // Retorna a quantidade total de elementos na coleção
    public int size() {
        return this.elements.size();
    }
}