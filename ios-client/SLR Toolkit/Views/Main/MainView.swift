import CoreData
import SwiftUI

/// Root view of the app.
struct MainView: View {
    @State var project: Project?

    var body: some View {
        if project != nil {
            NavigationView {
                ProjectView(project: $project)
            }
            .navigationViewStyle(DoubleColumnNavigationViewStyle())  // TODO always show sidebar in portrait mode (not possible as of SwiftUI 2)
        } else {
            WelcomeView(project: $project)
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            MainView(project: nil)
            MainView(project: nil)
                .colorScheme(.dark)
        }
    }
}
