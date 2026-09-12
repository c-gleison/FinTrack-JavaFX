package com.cg.fintrackgui.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repositório em memória para gerenciamento flexível de entidades.
 */
public class GenericRepository<T> {

    private final List<T> elements;

    // Inicializa o repositório com uma lista vazia na memória RAM
    public GenericRepository() {
        this.elements = new ArrayList<>();
    }
    
    // Inicializa o repositório populando com elementos de T ou suas subclasses (? extends T)
    public GenericRepository(List<? extends T> initialElements) {
        this.elements = new ArrayList<>();
        if (initialElements != null) {
            this.elements.addAll(initialElements);   
        }
    }

    // Adiciona um único elemento válido à coleção em memória
    public void add(T element) {
        if (element != null) {
            this.elements.add(element);            
        }
    }

    // Adiciona elementos do tipo T ou seus descendentes à coleção
    public void addAll(List<? extends T> sourceList) {
        if (sourceList != null) {
            this.elements.addAll(sourceList);      
        }
    }

    // Copia os elementos armazenados para uma lista de destino que aceite T ou seus ancestrais (? super T)
    public void copyTo(List<? super T> destinationList) {
        if (destinationList != null) {
            destinationList.addAll(this.elements);
        }
    }

    // Retorna uma visualização imutável (apenas leitura) dos elementos em memória
    public List<T> findAll() {
        return Collections.unmodifiableList(this.elements);
    }

    // Remove um elemento específico da lista interna
    public boolean remove(T element) {
        return this.elements.remove(element);
    }

    // Esvazia completamente a coleção armazenada na memória
    public void clear() {
        this.elements.clear();
    }

    // Retorna a quantidade total de elementos armazenados na memória
    public int size() {
        return this.elements.size();
    }
}