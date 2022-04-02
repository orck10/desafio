package br.com.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataPresenter<T, Y> {
	public final T data;
	public final Y meta;
}
