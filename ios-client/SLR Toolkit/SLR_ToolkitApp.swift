//
//  SLR_ToolkitApp.swift
//  SLR Toolkit
//
//  Created by Max Härtwig on 04.11.20.
//

import SwiftUI

@main
struct SLR_ToolkitApp: App {
    let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}
