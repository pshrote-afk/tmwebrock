package com.thinking.machines.webrock.model;

import java.util.*;
import com.thinking.machines.webrock.pojo.*;

public class WebRockModel
{
private Map<String,Service> servicesMap;
public WebRockModel()
{
this.servicesMap = new HashMap<>();
}
public Map<String,Service> getServicesMap()
{
return this.servicesMap;
}
}