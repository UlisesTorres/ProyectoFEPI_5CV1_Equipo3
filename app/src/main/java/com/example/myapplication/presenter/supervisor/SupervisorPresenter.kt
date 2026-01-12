package com.example.myapplication.presenter.supervisor

import com.example.myapplication.view.supervisor.SupervisorContract

class SupervisorPresenter(private val view: SupervisorContract.View) : SupervisorContract.Presenter{
    override fun clickConfiguracion() = view.navegarAConfiguracion()
}