package com.r42914lg.arkados.vitalk.graph;

import androidx.appcompat.app.AppCompatActivity;

import com.r42914lg.arkados.vitalk.ui.FirstFragment;
import com.r42914lg.arkados.vitalk.ui.MainActivity;
import com.r42914lg.arkados.vitalk.ui.SecondFragment;
import com.r42914lg.arkados.vitalk.ui.ThirdFragment;
import com.r42914lg.arkados.vitalk.ui.WorkItemAdapter;


import dagger.BindsInstance;
import dagger.Component;

@ScreenScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(FirstFragment fragment);
    void inject(SecondFragment fragment);
    void inject(ThirdFragment fragment);
    void inject(WorkItemAdapter adapter);

    @Component.Factory
    interface Factory {
        ActivityComponent create(AppComponent  appComponent,
                                 @BindsInstance AppCompatActivity activity);
    }
}
