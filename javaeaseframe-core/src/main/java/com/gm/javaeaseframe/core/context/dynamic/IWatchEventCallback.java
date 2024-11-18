package com.gm.javaeaseframe.core.context.dynamic;

public interface IWatchEventCallback
{
	public void callback(String fileName, String filePath, WatchEventKind kind);
}
